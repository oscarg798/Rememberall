package com.oscarg798.remembrall.taskdetails

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.formatters.DueDateFormatter
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.usecase.GetTaskById
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.taskdetails.usecase.GetTaskUpdateListenerUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    private val getTaskById: GetTaskById,
    private val dueDateFormatter: DueDateFormatter,
    private val getTaskUpdateListenerUseCase: GetTaskUpdateListenerUseCase,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<TaskDetailsViewModel.ViewState, TaskDetailsViewModel.Event>(
    ViewState(),
    coroutineContextProvider
) {

    init {
        getTask()

        viewModelScope.launch {
            getTaskUpdateListenerUseCase.execute()
                .collect {
                    if (it?.id == taskId) {
                        getTask()
                    }
                }
        }
    }

    private fun getTask() {
        viewModelScope.launch {
            update { it.copy(loading = true) }

            val task = withContext(coroutineContextProvider.io) {
                getTaskById.execute(taskId)
            }
            update {
                it.copy(loading = false, task = DisplayableTask(task, dueDateFormatter))
            }
        }
    }

    fun onEditClicked() {
        _event.tryEmit(Event.NavigateToEdit(taskId))
    }

    @AssistedFactory
    interface TaskDetailsViewModelFactory {

        fun create(taskId: String): TaskDetailsViewModel
    }

    data class ViewState(
        val loading: Boolean = false,
        val task: DisplayableTask? = null
    )

    sealed interface Event {
        data class NavigateToEdit(val taskId: String) : Event
    }
}
