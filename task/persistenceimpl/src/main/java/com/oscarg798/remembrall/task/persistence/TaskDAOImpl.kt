package com.oscarg798.remembrall.task.persistence

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.persistence.TaskDAO
import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.task.CalendarSyncInformation
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskPriority
import com.oscarg798.remembrall.task.persistence.dao.CalendarSyncInformationDAO
import com.oscarg798.remembrall.task.persistence.dao.RoomTaskDAO
import com.oscarg798.remembrall.task.persistence.entity.CalendarSyncInformationEntity
import com.oscarg798.remembrall.task.persistence.entity.TaskEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class TaskDAOImpl @Inject constructor(
    private val gson: Gson,
    private val roomTaskDAO: RoomTaskDAO,
    coroutineContextProvider: CoroutineContextProvider,
    private val calendarSyncInformationDAO: CalendarSyncInformationDAO,
) : TaskDAO, CoroutineContextProvider by coroutineContextProvider {

    private val attendeesType by lazy {
        object : TypeToken<ArrayList<String>>() {}.type
    }

    override suspend fun insert(tasks: List<Task>) {
        tasks.forEach { insert(it) }
    }

    override suspend fun insert(task: Task) {
        val calendarSyncInformationId = if (task.calendarSyncInformation != null) {
            val calendarInfo = task.calendarSyncInformation!!
            val calendarEntity = CalendarSyncInformationEntity(
                "${calendarInfo.calendarId}_${calendarInfo.calendarEventId}",
                eventId = calendarInfo.calendarEventId,
                synced = calendarInfo.synced,
                attendees = calendarInfo.attendees?.map { it.email }?.let { gson.toJson(it) },
                taskId = task.id,
                calendarId = calendarInfo.calendarId
            )
            withContext(io) { calendarSyncInformationDAO.insert(calendarEntity) }
            calendarEntity.id
        } else {
            null
        }

        withContext(io) {
            roomTaskDAO.insert(
                listOf(
                    TaskEntity(
                        id = task.id,
                        owner = task.owner,
                        owned = task.owned,
                        title = task.title,
                        priority = task.priority?.javaClass?.name,
                        calendarSyncInformation = calendarSyncInformationId,
                        dueDate = task.dueDate,
                        completed = task.completed,
                        description = task.description,
                        createdAt = task.createAt
                    )
                )
            )
        }
    }

    override fun get(id: String): Flow<Task> = roomTaskDAO.get(id)
        .filterNotNull()
        .flatMapLatest {
            flowOf(getTaskFromTaskEntity(it))
        }.flowOn(io)

    private suspend fun getTaskFromTaskEntity(taskEntity: TaskEntity): Task {
        val calendarInformation = if (taskEntity.calendarSyncInformation != null) {
            val calendarInfo = withContext(io) {
                calendarSyncInformationDAO.get(taskEntity.calendarSyncInformation)
            }
            val attendees = if (calendarInfo.attendees != null) {
                gson.fromJson<List<String>>(calendarInfo.attendees, attendeesType)
            } else {
                null
            }
            CalendarSyncInformation(
                calendarInfo.calendarId,
                calendarInfo.eventId,
                calendarInfo.synced,
                attendees?.let { attendees.map { CalendarAttendee(it) } }
            )
        } else {
            null
        }

        return Task(
            id = taskEntity.id,
            owner = taskEntity.owner,
            owned = taskEntity.owned,
            title = taskEntity.title,
            priority = taskEntity.priority?.let { TaskPriority.fromName(it) },
            calendarSyncInformation = calendarInformation,
            dueDate = taskEntity.dueDate,
            completed = taskEntity.completed,
            description = taskEntity.description,
            createAt = taskEntity.createdAt
        )
    }

    override fun stream(user: String): Flow<List<Task>> = roomTaskDAO.stream(user).flatMapLatest {
        flowOf(it.map { taskEntity -> getTaskFromTaskEntity(taskEntity) })
    }.flowOn(io)

    override suspend fun delete(id: String) {
        val exists = withContext(io) { roomTaskDAO.count(id) } > 0

        if (!exists) {
            error("Can not delete non existent task $id")
        }

        delete(get(id).first())
    }

    override suspend fun delete(task: Task) {
        val exists = withContext(io) { roomTaskDAO.count(task.id) } > 0

        if (!exists) {
            error("Can not delete non existent task ${task.id}")
        }

        val calendarInformationId = task.calendarSyncInformation?.let {
            "${it.calendarId}_${it.calendarEventId}"
        }

        if (calendarInformationId != null &&
            withContext(io) { calendarSyncInformationDAO.count(calendarInformationId) } > 0
        ) {
            withContext(io) {
                val calendarInfo = calendarSyncInformationDAO.get(calendarInformationId)
                calendarSyncInformationDAO.delete(calendarInfo)
            }
        }

        withContext(io) {
            val taskEntity = roomTaskDAO.get(task.id).first()
            roomTaskDAO.delete(taskEntity)
        }
    }

    override suspend fun update(task: Task) {
        val exists = withContext(io) { roomTaskDAO.count(task.id) } > 0

        if (!exists) {
            error("task with id ${task.id} not found, Are you trying to insert instead")
        }

        val calendarInfo = withContext(io) {
            calendarSyncInformationDAO.getByTaskId(task.id)
        }

        val calendarSyncInformationId = if (calendarInfo != null &&
            task.calendarSyncInformation == null
        ) {
            withContext(io) { calendarSyncInformationDAO.delete(calendarInfo) }
            null
        } else if (calendarInfo != null) {
            val calendarInfoToUpdate = task.calendarSyncInformation!!
            withContext(io) {
                val id =
                    "${calendarInfoToUpdate.calendarId}_${calendarInfoToUpdate.calendarEventId}"
                calendarSyncInformationDAO.update(
                    CalendarSyncInformationEntity(
                        id = id,
                        eventId = calendarInfoToUpdate.calendarEventId,
                        synced = calendarInfoToUpdate.synced,
                        attendees = calendarInfoToUpdate.attendees?.map { it.email }
                            ?.let { gson.toJson(it) },
                        taskId = task.id,
                        calendarId = calendarInfoToUpdate.calendarId
                    )
                )
                id
            }
        } else if (task.calendarSyncInformation != null) {
            val calendarInfoToInsert = task.calendarSyncInformation!!
            val id =
                "${calendarInfoToInsert.calendarId}_${calendarInfoToInsert.calendarEventId}"
            withContext(io) {
                calendarSyncInformationDAO.insert(
                    CalendarSyncInformationEntity(
                        id = id,
                        eventId = calendarInfoToInsert.calendarEventId,
                        synced = calendarInfoToInsert.synced,
                        attendees = calendarInfoToInsert.attendees?.map { it.email }
                            ?.let { gson.toJson(it) },
                        taskId = task.id,
                        calendarId = calendarInfoToInsert.calendarId
                    )
                )
            }
            id
        } else {
            null
        }

        withContext(io) {
            roomTaskDAO.update(
                TaskEntity(
                    id = task.id,
                    owner = task.owner,
                    owned = task.owned,
                    title = task.title,
                    priority = task.priority?.javaClass?.name,
                    calendarSyncInformation = calendarSyncInformationId,
                    dueDate = task.dueDate,
                    completed = task.completed,
                    description = task.description,
                    createdAt = task.createAt
                )
            )
        }
    }

    override suspend fun exists(id: String): Boolean = withContext(io) {
        roomTaskDAO.count(id) > 0
    }
}