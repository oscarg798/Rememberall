package com.oscarg798.remembrall.common_task.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.oscarg798.remembrall.common.toSuspend
import com.oscarg798.remembrall.common_task.TaskDataSourceException
import com.oscarg798.remembrall.common_task.TaskNotFoundException
import com.oscarg798.remembrall.common_task.model.TaskDto
import javax.inject.Inject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class FirebaseTaskStoreDataSource @Inject constructor(private val taskCollection: CollectionReference) :
    TaskDataSource {

    override suspend fun addTask(user: String, taskDto: TaskDto) {
        taskCollection.document(taskDto.id).set(
            taskDto.toMap().toMutableMap().apply {
                put(UserColumnName, user)
            }
        ).toSuspend {
            TaskDataSourceException.UnableToAddTask(
                it
            )
        }
    }

    override suspend fun getTask(id: String): TaskDto {
        val taskResult = taskCollection.document(id).get().toSuspend {
            TaskNotFoundException(id)
        }

        return TaskDto(id, taskResult.result.data!!)
    }

    override suspend fun getTasks(user: String): Collection<TaskDto> {
        val tasks = mutableListOf<Task<QuerySnapshot>>()
        tasks.add(getQuerySnapshot(taskCollection.whereEqualTo(UserColumnName, user)))
        tasks.add(getQuerySnapshot(taskCollection.whereArrayContainsAny(AttendeesColumnName, listOf(user))))

        val results = tasks

        return results.map {
            it.result.documents
        }.map {
            it.map { document ->
                TaskDto(document.id, document.data!!)
            }
        }.flatten()
    }

    private suspend fun getQuerySnapshot(query: Query): Task<QuerySnapshot> = coroutineScope {
        query.get().toSuspend {
            TaskDataSourceException.UnableToLoadTasks(it)
        }
    }

    override suspend fun deleteTask(id: String) {
        taskCollection.document(id).update(
            mapOf(
                TaskDto.ColumnNames.Completed to true
            )
        ).toSuspend {
            TaskDataSourceException.UnableToDeleteTask(taskId = id, cause = it)
        }
    }

    override suspend fun update(task: TaskDto) {
        taskCollection.document(task.id).update(task.toMap())
            .toSuspend {
                TaskDataSourceException.UnableToUpdateTask(task.id)
            }
    }
}

private const val AttendeesColumnName = "attendees"
private const val UserColumnName = "user"
