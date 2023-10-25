package com.oscarg798.remembrall.task

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun addTask(user: String, addTaskParam: AddTaskParam): Task

    @Deprecated(
        "Stream tasks instead",
        replaceWith = ReplaceWith("streamTasks(user: String)")
    )
    suspend fun getTasks(user: String): Collection<Task>

    suspend fun getTask(id: String): Task

    fun streamTask(id: String): Flow<Task>

    suspend fun removeTask(id: String)

    suspend fun update(task: Task)

    fun streamTasks(user: String): Flow<Collection<Task>>

    fun streamTasks(queries: List<TaskQuery>, queryOperation: QueryOperation): Flow<List<Task>>

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

    sealed interface TaskQuery {
        data class Completed(val value: Boolean): TaskQuery
        data class UserEquals(val value: String) : TaskQuery
        data class DueDateBefore(val value: Long) : TaskQuery
        data class DueDateAfter(val value: Long) : TaskQuery
    }

    enum class QueryOperation {
        And,
        Or
    }
}
