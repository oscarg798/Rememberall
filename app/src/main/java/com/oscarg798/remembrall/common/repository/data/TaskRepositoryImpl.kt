package com.oscarg798.remembrall.common.repository.data

import com.oscarg798.remembrall.common.datasource.TaskDataSource
import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.network.dto.TaskDto
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TaskRepositoryImpl(private val taskDataSource: TaskDataSource) : TaskRepository {

    private val editableTaskUpdateListener = MutableStateFlow<Task?>(null)

    override val taskUpdateListener: Flow<Task?>
        get() = editableTaskUpdateListener

    override suspend fun addTask(addTaskParam: TaskRepository.AddTaskParam): Task {
        val task = TaskDto(
            id = UUID.randomUUID().toString(),
            name = addTaskParam.name,
            description = addTaskParam.description,
            priority = addTaskParam.priority,
            completed = false,
            dueDate = addTaskParam.dueDate
        )

        taskDataSource.addTask(task)

        return task.toTask()
    }

    override suspend fun updateWithCalendarInformation(
        tasksCalendarSyncInformation: CalendarSyncInformation,
        task: Task
    ) {
        taskDataSource.update(TaskDto(task, tasksCalendarSyncInformation))
    }

    override suspend fun update(task: Task) {
        taskDataSource.update(TaskDto(task))
    }

    override suspend fun removeTask(id: String) {
        taskDataSource.deleteTask(id)
    }

    override suspend fun getTasks(): Collection<Task> =
        taskDataSource.getTasks().map { it.toTask() }

    override suspend fun getTask(id: String): Task = taskDataSource.getTask(id).toTask()

    override fun onTaskUpdated(task: Task) {
        editableTaskUpdateListener.value = task
    }
}
