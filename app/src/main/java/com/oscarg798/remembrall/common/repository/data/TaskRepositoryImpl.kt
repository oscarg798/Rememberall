package com.oscarg798.remembrall.common.repository.data

import com.oscarg798.remembrall.common.datasource.TaskDataSource
import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.network.dto.TaskDto
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import java.util.UUID

class TaskRepositoryImpl(private val taskDataSource: TaskDataSource) : TaskRepository {

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

    override suspend fun removeTask(id: String) {
        taskDataSource.deleteTask(id)
    }

    override suspend fun getTasks(): Collection<Task> =
        taskDataSource.getTasks().map { it.toTask() }

    override suspend fun getTask(id: String): Task = taskDataSource.getTask(id).toTask()
}
