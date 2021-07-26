package com.oscarg798.remembrall.common.repository.domain

import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.model.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    val taskUpdateListener: Flow<Task?>

    suspend fun addTask(addTaskParam: AddTaskParam): Task

    suspend fun getTasks(): Collection<Task>

    suspend fun getTask(id: String): Task

    suspend fun removeTask(id: String)

    suspend fun updateWithCalendarInformation(
        tasksCalendarSyncInformation: CalendarSyncInformation,
        task: Task
    )

    suspend fun update(task: Task)

    fun onTaskUpdated(task: Task)

    data class AddTaskParam(
        val name: String,
        val priority: TaskPriority,
        val description: String? = null,
        val dueDate: Long? = null
    )
}
