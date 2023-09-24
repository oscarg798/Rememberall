package com.oscarg798.remembrall.tasklist


import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_task.GetTaskUpdateListenerUseCase
import com.oscarg798.remembrall.common_task.ui.TaskCardOptions
import com.oscarg798.remembrall.dateformatter.DueDateFormatter
import com.oscarg798.remembrall.tasklist.model.DisplayableTasksGroup
import com.oscarg798.remembrall.tasklist.model.TaskGroup
import com.oscarg798.remembrall.tasklist.usecase.GetInitialIndexPosition
import com.oscarg798.remembrall.tasklist.usecase.GetTaskGrouped
import com.oscarg798.remembrall.tasklist.usecase.RemoveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.withContext

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val getTaskGrouped: GetTaskGrouped,
    private val getInitialIndexPosition: GetInitialIndexPosition,
    private val dueDateFormatter: DueDateFormatter,
    private val getTaskUpdateListenerUseCase: GetTaskUpdateListenerUseCase,
    coroutineContextProvider: com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
) : AbstractViewModel<TaskListViewModel.ViewState, TaskListViewModel.Event>(
    ViewState()
), com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider by coroutineContextProvider {

    private var shouldUpdate = false

    init {
        launch {
            getTaskUpdateListenerUseCase.execute().collect {
                if (it != null) {
                    shouldUpdate = true
                }
            }
        }
    }

    fun onAddClicked() {
        _event.tryEmit(Event.ShowAddTaskForm)
    }

    fun getTasks() = launch {
        if (currentState().tasks.isEmpty() || shouldUpdate) {
            shouldUpdate = false
            fetchTasks()
        }
    }

    private fun fetchTasks() = launch {
        update { it.copy(loading = true, error = null) }

        val tasks = withContext(io) {
            getTaskGrouped()
        }

        val index = withContext(io) { getInitialIndexPosition(tasks) }

        val displayableTasks = withContext(io) {
            tasks.map {
                it.key to DisplayableTasksGroup(it.value, dueDateFormatter)
            }.toMap()
        }

        update { currentState ->
            currentState.copy(loading = false, tasks = displayableTasks, initialIndex = index)
        }
    }


    fun onOptionClicked(task: DisplayableTask, option: TaskCardOptions.Option) {
        when (option) {
            TaskCardOptions.Option.Edit -> _event.tryEmit(Event.NavigateToEdit(task.id))
            TaskCardOptions.Option.Remove -> launch {
                withContext(io) {
                    removeTaskUseCase.execute(task.id)
                }

                getTasks()
            }
        }
    }

    data class ViewState(
        val loading: Boolean = true,
        val tasks: Map<TaskGroup.MonthGroup, DisplayableTasksGroup> = mapOf(),
        val initialIndex: Int = -1,
        val options: List<TaskCardOptions.Option> = listOf(
            TaskCardOptions.Option.Edit,
            TaskCardOptions.Option.Remove
        ),
        @Deprecated("Errors should be model as events instead of state property")
        val error: Exception? = null
    )

    sealed class Event {
        object OpenProfile : Event()
        object ShowAddTaskForm : Event()
        data class NavigateToEdit(val taskId: String): Event()
    }
}
