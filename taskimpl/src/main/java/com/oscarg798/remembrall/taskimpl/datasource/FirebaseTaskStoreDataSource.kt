package com.oscarg798.remembrall.taskimpl.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.gmstaskutils.toSuspend
import com.oscarg798.remembrall.taskimpl.model.CalendarSyncInformationDto
import com.oscarg798.remembrall.taskimpl.model.TaskDto
import javax.inject.Inject
import kotlinx.coroutines.withContext

internal class FirebaseTaskStoreDataSource @Inject constructor(
    private val taskCollection: CollectionReference,
    private val coroutinesContextProvider: CoroutineContextProvider
) : TaskDataSource {

    override suspend fun addTask(user: String, taskDto: TaskDto) {
        withContext(coroutinesContextProvider.io) {
            taskCollection.document(taskDto.id).set(
                taskDto.toPairColumns().toMap().toMutableMap().apply {
                    put(UserColumnName, user)
                }
            ).toSuspend {
                IllegalStateException("not able to add task ${taskDto.id}", it)
            }
        }
    }

    override suspend fun getTask(id: String): TaskDto {
        val taskResult = withContext(coroutinesContextProvider.io) {
            taskCollection.document(id).get().toSuspend {
                IllegalArgumentException("Task with id $id not found", it)
            }
        }

        return TaskDto(id, taskResult.result.data!!)
    }

    override suspend fun getTasks(user: String): Collection<TaskDto> {
        val taskQueries = mutableListOf<Task<QuerySnapshot>>()
        taskQueries.add(getQuerySnapshot(taskCollection.whereEqualTo(UserColumnName, user)))
        taskQueries.add(
            getQuerySnapshot(
                taskCollection.whereArrayContainsAny(
                    AttendeesColumnName,
                    listOf(user)
                )
            )
        )

        return withContext(coroutinesContextProvider.computation) {
            taskQueries.map {
                it.result.documents
            }.map {
                it.map { document ->
                    TaskDto(document.id, document.data!!)
                }
            }.flatten()
        }
    }

    private suspend fun getQuerySnapshot(query: Query): Task<QuerySnapshot> =
        withContext(coroutinesContextProvider.io) {
            query.get().toSuspend { cause ->
                IllegalStateException("Can not perform query", cause)
            }
        }

    override suspend fun deleteTask(id: String) {
        withContext(coroutinesContextProvider.io) {
            taskCollection.document(id).update(
                mapOf(
                    TaskDto.ColumnNames.Completed to true
                )
            ).toSuspend {
                IllegalStateException("not able to delete task $id", it)
            }
        }
    }

    override suspend fun update(task: TaskDto) {
        withContext(coroutinesContextProvider.io) {
            taskCollection.document(task.id).update(task.toPairColumns().toMap())
                .toSuspend {
                    IllegalStateException("not able to update task ${task.id}", it)
                }
        }
    }

    private fun TaskDto.toPairColumns(): List<Pair<String, Any?>> {
        val pairs = mutableListOf<Pair<String, Any?>>(
            TaskDto.ColumnNames.Name to name,
            TaskDto.ColumnNames.Description to description,
            TaskDto.ColumnNames.Completed to completed
        )

        dueDate?.let {
            pairs.add(TaskDto.ColumnNames.DueDate to dueDate)
        }
        priority?.let {
            pairs.add(TaskDto.ColumnNames.Priority to priority.javaClass.name)
        }

        createdAt?.let {
            pairs.add(TaskDto.ColumnNames.CreatedAt to createdAt)
        }

        calendarSyncInformation?.let {
            pairs.add(
                CalendarSyncInformationDto.ColumnNames.CalendarId to
                        calendarSyncInformation.calendarId
            )
            pairs.add(
                CalendarSyncInformationDto.ColumnNames.CalendarEventId to
                        calendarSyncInformation.calendarEventId
            )
            pairs.add(
                CalendarSyncInformationDto.ColumnNames.Synced to
                        calendarSyncInformation.synced
            )
            calendarSyncInformation.attendees?.map { it.email }?.let { emails ->
                pairs.add(CalendarSyncInformationDto.ColumnNames.Attendees to emails)
            }
        }

        return pairs
    }
}

private const val AttendeesColumnName = "attendees"
private const val UserColumnName = "user"
