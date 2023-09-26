package com.oscarg798.remembrall.taskimpl

import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.taskimpl.datasource.TaskDataSource
import com.oscarg798.remembrall.taskimpl.model.CalendarSyncInformationDto
import com.oscarg798.remembrall.taskimpl.model.TaskDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class TaskRepositoryImpl @Inject constructor(
    private val idProvider: IdProvider,
    private val taskDataSource: TaskDataSource,
) : TaskRepository {

    private val editableTaskUpdateListener = MutableStateFlow<Task?>(null)

    override val taskUpdateListener: Flow<Task>
        get() = editableTaskUpdateListener.filterNotNull()

    override suspend fun addTask(user: String, addTaskParam: TaskRepository.AddTaskParam): Task {
        val task = TaskDto(
            id = addTaskParam.id,
            owner = user,
            name = addTaskParam.name,
            description = addTaskParam.description,
            priority = addTaskParam.priority,
            completed = false,
            dueDate = addTaskParam.dueDate,
            calendarSyncInformation = addTaskParam.calendarSyncInformation?.let {
                CalendarSyncInformationDto(it)
            }
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
        taskDataSource.getTasks(user).map {
            it.toTask(
                it.owner == user
            )
        }

    override suspend fun getTask(id: String): Task = taskDataSource.getTask(id).toTask()

    override fun onTaskUpdated(task: Task) {
        editableTaskUpdateListener.value = task
    }

    override fun createTaskId(): String = idProvider()
}
