package com.oscarg798.remembrall.common.datasource

import com.oscarg798.remembrall.common.network.dto.TaskDto

interface TaskDataSource {

    suspend fun addTask(taskDto: TaskDto)

    suspend fun getTask(id: String): TaskDto

    suspend fun getTasks(): Collection<TaskDto>

    suspend fun deleteTask(id: String)

    suspend fun update(task: TaskDto)
}
