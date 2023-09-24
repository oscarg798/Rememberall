package com.oscarg798.remembrall.taskdetail

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_gettask.usecase.GetTaskById
import com.oscarg798.remembrall.common_task.GetTaskUpdateListenerUseCase
import com.oscarg798.remembrall.dateformatter.DueDateFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    private val getTaskById: GetTaskById,
    private val dueDateFormatter: DueDateFormatter,
    private val getTaskUpdateListenerUseCase: GetTaskUpdateListenerUseCase,
    coroutineContextProvider: com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
) : AbstractViewModel<TaskDetailsViewModel.ViewState, TaskDetailsViewModel.Event>(
    ViewState()
), com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider by coroutineContextProvider {

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
        launch {
            update { it.copy(loading = true) }

            val task = withContext(io) {
                DisplayableTask(
                    task = getTaskById.execute(taskId),
                    dueDateFormatter = dueDateFormatter
                )
            }
            update {
                it.copy(loading = false, task = task)
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
