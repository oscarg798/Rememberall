package com.oscarg798.remembrall.common_task.datasource

import com.google.firebase.firestore.CollectionReference
import com.oscarg798.remembrall.common_task.TaskDataSourceException
import com.oscarg798.remembrall.common_task.TaskNotFoundException
import com.oscarg798.remembrall.common_task.model.TaskDto
import com.oscarg798.remembrall.common.toSuspend
import javax.inject.Inject

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
        val taskResult = taskCollection.whereEqualTo(UserColumnName, user).get().toSuspend {
            TaskDataSourceException.UnableToLoadTasks(it)
        }

        return taskResult.result.documents.map {
            TaskDto(it.id, it.data!!)
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

private const val UserColumnName = "user"
