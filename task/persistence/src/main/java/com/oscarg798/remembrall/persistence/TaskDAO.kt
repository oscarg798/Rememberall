package com.oscarg798.remembrall.persistence

import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import kotlinx.coroutines.flow.Flow

interface TaskDAO {

    suspend fun insert(tasks: List<Task>)

    suspend fun insert(task: Task)

    fun get(id: String): Flow<Task>

    fun streamViaQuery(
        queries: List<TaskRepository.TaskQuery>,
        queryOperation: TaskRepository.QueryOperation
    ): Flow<List<Task>>

    fun stream(user: String): Flow<List<Task>>

    suspend fun delete(task: Task)

    suspend fun delete(id: String)

    suspend fun update(task: Task)

    suspend fun exists(id: String): Boolean
}