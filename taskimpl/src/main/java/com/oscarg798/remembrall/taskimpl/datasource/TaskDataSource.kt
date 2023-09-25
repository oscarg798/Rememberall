package com.oscarg798.remembrall.taskimpl.datasource

import com.oscarg798.remembrall.taskimpl.model.TaskDto

internal interface TaskDataSource {

    suspend fun addTask(user: String, taskDto: TaskDto)

    suspend fun getTask(id: String): TaskDto

    suspend fun getTasks(user: String): Collection<TaskDto>

    suspend fun deleteTask(id: String)

    suspend fun update(task: TaskDto)
}