package com.oscarg798.remembrall.taskimpl

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.persistence.TaskDAO
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.taskimpl.datasource.TaskDataSource
import com.oscarg798.remembrall.taskimpl.model.CalendarSyncInformationDto
import com.oscarg798.remembrall.taskimpl.model.TaskDto
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class TaskRepositoryImpl @Inject constructor(
    private val taskDAO: TaskDAO,
    private val idProvider: IdProvider,
    private val taskDataSource: TaskDataSource,
    private val coroutinesContextProvider: CoroutineContextProvider,
) : TaskRepository {

    override suspend fun addTask(user: String, addTaskParam: TaskRepository.AddTaskParam): Task {
        val task = TaskDto(
            id = addTaskParam.id,
            owner = user,
            name = addTaskParam.name,
            description = addTaskParam.description,
            priority = addTaskParam.priority,
            completed = false,
            dueDate = addTaskParam.dueDate,
            calendarSyncInformation = addTaskParam.calendarSyncInformation?.let {
                CalendarSyncInformationDto(it)
            }
        )

        taskDataSource.addTask(user, task)

        val addedTask = task.toTask(true)
        taskDAO.insert(addedTask)

        return addedTask
    }

    override suspend fun update(task: Task) {
        taskDataSource.update(TaskDto(task))
        taskDAO.update(task)
    }

    override suspend fun removeTask(id: String) {
        taskDataSource.deleteTask(id)
        withContext(coroutinesContextProvider.computation) {
            taskDAO.delete(id)
        }

        if (taskDAO.exists(id)) {
            taskDAO.delete(id)
        }
    }

    @Deprecated(
        message = "Stream tasks instead",
        replaceWith = ReplaceWith("streamTasks(user: String)")
    )
    override suspend fun getTasks(user: String): Collection<Task> =
        taskDataSource.getTasks(user).map {
            it.toTask(
                it.owner == user
            )
        }

    override fun streamTasks(user: String): Flow<Collection<Task>> = flow {
        fetchTasks(user)
        emitAll(taskDAO.stream(user))
    }

    override fun streamTasks(
        queries: List<TaskRepository.TaskQuery>,
        queryOperation: TaskRepository.QueryOperation
    ): Flow<List<Task>> = flow {
        fetchTask(queries, queryOperation)
        emitAll(taskDAO.streamViaQuery(queries, queryOperation))
    }

    private suspend fun fetchTask(
        queries: List<TaskRepository.TaskQuery>,
        queryOperation: TaskRepository.QueryOperation
    ) = coroutineScope {
        launch {
            val tasks = taskDataSource.getTasks(queries, queryOperation).map {
                it.toTask(false)
            }
            taskDAO.insert(tasks)
        }
    }

    private suspend fun fetchTasks(user: String) = coroutineScope {
        launch {
            taskDAO.insert(getTasks(user).toList())
        }
    }

    override suspend fun getTask(id: String): Task {
        val task = taskDataSource.getTask(id).toTask()
        if (taskDAO.exists(id)) {
            taskDAO.update(task)
        } else {
            taskDAO.insert(task)
        }
        return task
    }

    override fun createTaskId(): String = idProvider()

    override fun streamTask(id: String): Flow<Task> = taskDAO.get(id).onStart {
        getTask(id)
    }

    private fun TaskDto.toTask(owned: Boolean = OwnershipUnknownAtThisPoint) = Task(
        id = id,
        title = name,
        owner = owner,
        description = description,
        priority = priority,
        completed = completed,
        calendarSyncInformation = calendarSyncInformation?.toCalendarSyncInformation(),
        dueDate = dueDate,
        owned = owned,
        createAt = createdAt
    )
}


private const val OwnershipUnknownAtThisPoint = false