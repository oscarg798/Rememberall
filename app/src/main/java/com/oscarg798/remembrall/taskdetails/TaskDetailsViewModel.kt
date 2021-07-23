package com.oscarg798.remembrall.taskdetails

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    private val getTaskById: GetTaskById,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<TaskDetailsViewModel.ViewState, TaskDetailsViewModel.Event>(
    ViewState(),
    coroutineContextProvider
) {

    init {
        viewModelScope.launch {
            update { it.copy(loading = true) }

            val task = withContext(coroutineContextProvider.io) {
                getTaskById.execute(taskId)
            }
            update {
                it.copy(loading = false, task = task)
            }
        }
    }

    @AssistedFactory
    interface TaskDetailsViewModelFactory {

        fun create(taskId: String): TaskDetailsViewModel
    }

    data class ViewState(
        val loading: Boolean = false,
        val task: Task? = null
    )

    object Event
}
