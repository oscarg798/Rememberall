package com.oscarg798.remembrall.taskimpl

import android.util.LruCache
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.taskimpl.datasource.TaskDataSource
import com.oscarg798.remembrall.taskimpl.model.CalendarSyncInformationDto
import com.oscarg798.remembrall.taskimpl.model.TaskDto
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class TaskRepositoryImpl @Inject constructor(
    private val idProvider: IdProvider,
    private val taskDataSource: TaskDataSource,
    private val coroutinesContextProvider: CoroutineContextProvider,
) : TaskRepository {

    private val streamableTasks = MutableStateFlow<Collection<Task>>(emptyList())

    private val taskByIdCache = LruCache<String, Task>(TaskByIdCacheSize)
    private val streamableTaskByIds = MutableStateFlow<Map<String, Task>>(mapOf())

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

        val addedTask = task.toTask(true)

        withContext(coroutinesContextProvider.computation) {
            val tasksId = streamableTasks.value.map { it.id }.toSet()
            if (addedTask.id !in tasksId) {
                streamableTasks.value = streamableTasks.value.toMutableList().apply {
                    add(addedTask)
                }
            }
        }

        return addedTask
    }

    override suspend fun update(task: Task) {
        taskDataSource.update(TaskDto(task))
        updateStreamableTasks(task)
        updateTaskByIdCache(task)
    }

    override suspend fun removeTask(id: String) {
        taskDataSource.deleteTask(id)
        withContext(coroutinesContextProvider.computation) {
            streamableTasks.value = streamableTasks.value.filter { it.id != id }
            val currentTaskByIdCache = taskByIdCache.snapshot()
            if (currentTaskByIdCache.containsKey(id)) {
                taskByIdCache.remove(id)
                streamableTaskByIds.value = taskByIdCache.snapshot()
            }
        }
    }

    @Deprecated(
        message = "Stream tasks instead",
        replaceWith = ReplaceWith("streamTasks(user: String)")
    )
    override suspend fun getTasks(user: String): Collection<Task> =
        taskDataSource.getTasks(user).map {
            it.toTask(
                it.owner == user
            )
        }

    override fun streamTasks(user: String): Flow<Collection<Task>> = streamableTasks.asStateFlow()
        .onStart {
            fetchTasks(user)
        }

    private suspend fun fetchTasks(user: String) = coroutineScope {
        launch { streamableTasks.value = getTasks(user) }
    }

    override suspend fun getTask(id: String): Task = taskDataSource.getTask(id).toTask()

    override fun createTaskId(): String = idProvider()

    override fun streamTask(id: String): Flow<Task> = streamableTaskByIds.asStateFlow()
        .map {
            taskByIdCache.snapshot()[id]
        }.filterNotNull()
        .onStart {
            updateTaskByIdCache(getTask(id))
        }.flowOn(coroutinesContextProvider.io)

    private suspend fun updateStreamableTasks(vararg tasks: Task) =
        withContext(coroutinesContextProvider.computation) {
            val currentTasks = streamableTasks.value.toMutableList()
            tasks.forEach { task ->
                val index = currentTasks.indexOfFirst { it.id == task.id }
                if (index != -1) {
                    currentTasks.removeAt(index)
                }
                currentTasks.add(task)
            }
            streamableTasks.value = currentTasks
        }

    private suspend fun updateTaskByIdCache(task: Task) =
        withContext(coroutinesContextProvider.computation) {
            taskByIdCache.put(task.id, task)
            streamableTaskByIds.value = taskByIdCache.snapshot()
        }

    private fun TaskDto.toTask(owned: Boolean = OwnershipUnknownAtThisPoint) = Task(
        id = id,
        title = name,
        owner = owner,
        description = description,
        priority = priority,
        completed = completed,
        calendarSyncInformation = calendarSyncInformation?.toCalendarSyncInformation(),
        dueDate = dueDate,
        owned = owned,
        createAt = createdAt
    )
}


private const val OwnershipUnknownAtThisPoint = false
private const val TaskByIdCacheSize = 3