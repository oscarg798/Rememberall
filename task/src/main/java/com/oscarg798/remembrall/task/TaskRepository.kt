package com.oscarg798.remembrall.task

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    @Deprecated(
        "Stream tasks instead",
        replaceWith = ReplaceWith("streamTasks(user: String)")
    )
    val taskUpdateListener: Flow<Task>

    suspend fun addTask(user: String, addTaskParam: AddTaskParam): Task

    @Deprecated(
        "Stream tasks instead",
        replaceWith = ReplaceWith("streamTasks(user: String)")
    )
    suspend fun getTasks(user: String): Collection<Task>

    suspend fun getTask(id: String): Task

    suspend fun removeTask(id: String)

    suspend fun update(task: Task)

    fun streamTasks(user: String): Flow<Collection<Task>>

    fun onTaskUpdated(task: Task)

    fun createTaskId(): String

    data class AddTaskParam(
        val id: String,
        val name: String,
        val priority: TaskPriority?,
        val calendarSyncInformation: CalendarSyncInformation?,
        val dueDate: Long?,
        val description: String? = null,
        val createdAt: Long?
    )
}
