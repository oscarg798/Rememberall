package com.oscarg798.remembrall.tasklist

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.usecase.GetSignedUserUseCase
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTask
import com.oscarg798.remembrall.tasklist.ui.model.TaskListScreenConfiguration
import com.oscarg798.remembrall.tasklist.ui.util.GetDisplayableTasks
import com.oscarg798.remembrall.tasklist.usecase.GetTaskUseCase
import com.oscarg798.remembrall.tasklist.usecase.RemoveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val getSignedUserUseCase: GetSignedUserUseCase,
    private val getDisplayableTasks: GetDisplayableTasks,
    private val getTasksUseCase: GetTaskUseCase,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<TaskListViewModel.ViewState, TaskListViewModel.Event>(
    ViewState(),
    coroutineContextProvider
) {

    fun getSignedInUser() {
        viewModelScope.launch {
            update { it.copy(userSessionStatus = ViewState.UserSessionStatus.Loading) }

            runCatching {
                withContext(coroutineContextProvider.io) {
                    getSignedUserUseCase.execute()
                }
            }.onSuccess { _ ->
                update { it.copy(userSessionStatus = ViewState.UserSessionStatus.SignedIn) }
            }.onFailure {
                update { it.copy(userSessionStatus = ViewState.UserSessionStatus.LoggedOut) }
            }
        }
    }

    fun onAddClicked() {
        _event.tryEmit(Event.ShowAddTaskForm)
    }

    fun getTasks() {
        viewModelScope.launch {

            update { it.copy(loading = true, error = null) }

            val screenConfiguration = withContext(coroutineContextProvider.io) {
                getDisplayableTasks.execute(getTasksUseCase.execute())
            }

            update { currentState ->
                currentState.copy(loading = false, screenConfiguration = screenConfiguration)
            }
        }
    }

    fun removeTask(task: DisplayableTask) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                removeTaskUseCase.execute(task.id)
            }
            getTasks()
        }
    }

    fun onProfileButtonClicked() {
        _event.tryEmit(Event.OpenProfile)
    }

    data class ViewState(
        val loading: Boolean = true,
        val screenConfiguration: TaskListScreenConfiguration? = null,
        val userSessionStatus: UserSessionStatus = UserSessionStatus.Loading,
        val error: Exception? = null
    ) {
        sealed class UserSessionStatus {

            object SignedIn : UserSessionStatus()
            object Loading : UserSessionStatus()
            object LoggedOut : UserSessionStatus()
        }
    }

    sealed class Event {
        object OpenProfile : Event()
        object ShowAddTaskForm : Event()
    }
}
