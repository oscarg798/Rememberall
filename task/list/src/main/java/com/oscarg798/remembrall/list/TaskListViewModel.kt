package com.oscarg798.remembrall.list

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.list.model.DisplayableTask
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.list.model.DisplayableTasksGroup
import com.oscarg798.remembrall.list.model.TaskGroup
import com.oscarg798.remembrall.list.ui.TaskCardOptions
import com.oscarg798.remembrall.list.usecase.GetInitialIndexPosition
import com.oscarg798.remembrall.list.usecase.GetTaskGrouped
import com.oscarg798.remembrall.list.usecase.RemoveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val getTaskGrouped: GetTaskGrouped,
    private val getInitialIndexPosition: GetInitialIndexPosition,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<TaskListViewModel.ViewState, TaskListViewModel.Event>(
    ViewState()
), CoroutineContextProvider by coroutineContextProvider {

    fun onAddClicked() {
        _event.tryEmit(Event.ShowAddTaskForm)
    }

    fun getTasks() = launch {
        if (currentState().tasks.isEmpty()) {
            fetchTasks()
        }
    }

    private fun fetchTasks() = launch {
        update { it.copy(loading = true, error = null) }

        getTaskGrouped()
            .flatMapMerge { displayableTasksMapped ->
                val index = withContext(io) { getInitialIndexPosition(displayableTasksMapped) }
                flowOf(Pair(index, displayableTasksMapped))
            }
            .collectLatest { indexAndDisplayableTasks ->
                update { currentState ->
                    currentState.copy(
                        loading = false,
                        tasks = indexAndDisplayableTasks.second,
                        initialIndex = indexAndDisplayableTasks.first
                    )
                }
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
        data class NavigateToEdit(val taskId: String) : Event()
    }
}
