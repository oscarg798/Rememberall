package com.oscarg798.remembrall.common_task

import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import com.oscarg798.remembrall.common_task.datasource.TaskDataSource
import com.oscarg798.remembrall.common_task.model.CalendarSyncInformationDto
import com.oscarg798.remembrall.common_task.model.TaskDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class TaskRepositoryImpl(private val taskDataSource: TaskDataSource) : TaskRepository {

    private val editableTaskUpdateListener = MutableStateFlow<Task?>(null)

    override val taskUpdateListener: Flow<Task?>
        get() = editableTaskUpdateListener

    override suspend fun addTask(user: String, addTaskParam: TaskRepository.AddTaskParam): Task {
        val task = TaskDto(
            id = addTaskParam.id,
            owner= user,
            name = addTaskParam.name,
            description = addTaskParam.description,
            priority = addTaskParam.priority,
            completed = false,
            dueDate = addTaskParam.dueDate,
            calendarSyncInformation = CalendarSyncInformationDto(addTaskParam.calendarSyncInformation)
        )

        taskDataSource.addTask(user, task)

        return task.toTask(true)
    }

    override suspend fun update(task: Task) {
        taskDataSource.update(TaskDto(task))
    }

    override suspend fun removeTask(id: String) {
        taskDataSource.deleteTask(id)
    }

    override suspend fun getTasks(user: String): Collection<Task> =
        taskDataSource.getTasks(user).map { it.toTask(
            it.owner == user
        ) }

    override suspend fun getTask(id: String): Task = taskDataSource.getTask(id).toTask()

    override fun onTaskUpdated(task: Task) {
        editableTaskUpdateListener.value = task
    }

    override fun createTaskId(): String = UUID.randomUUID().toString()
}
