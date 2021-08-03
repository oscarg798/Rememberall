package com.oscarg798.remembrall.addtask

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.addtask.exception.AddTaskException
import com.oscarg798.remembrall.addtask.ui.AddtaskScreenConfiguration
import com.oscarg798.remembrall.addtask.usecase.AddTaskUseCase
import com.oscarg798.remembrall.addtask.usecase.GetAvailablePrioritiesUseCase
import com.oscarg798.remembrall.addtask.usecase.GetDisplayableDueDate
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common_auth.usecase.GetSignedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val getFormattedDueDate: GetDisplayableDueDate,
    private val addTaskUseCase: AddTaskUseCase,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val getAvailablePrioritiesUseCaseEdit: GetAvailablePrioritiesUseCase,
    private val stringProvider: StringProvider,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<AddTaskViewModel.ViewState, AddTaskViewModel.Event>(
    ViewState(),
    coroutineContextProvider
) {

    init {
        verifyUserSession(coroutineContextProvider)
        getAvailablePriorities(coroutineContextProvider)
        getCurrentDatePlaceHolder()
    }

    private fun getCurrentDatePlaceHolder() {
        if (currentState().formattedDueDate != null) {
            return
        }

        viewModelScope.launch {
            val date = getFormattedDueDate.execute(LocalDateTime.now())
            update {
                it.copy(formattedDueDate = date)
            }
        }
    }

    private fun getAvailablePriorities(coroutineContextProvider: CoroutineContextProvider) {
        viewModelScope.launch {
            val availablePriorities = withContext(coroutineContextProvider.computation) {
                getAvailablePrioritiesUseCaseEdit.execute()
            }

            val screenConfiguration = AddtaskScreenConfiguration(
                availablePriorities,
                TaskPriority.Low
            )
            update {
                it.copy(
                    addtaskScreenConfiguration = screenConfiguration,
                    priority = screenConfiguration.selectedPriority
                )
            }
        }
    }

    private fun verifyUserSession(coroutineContextProvider: CoroutineContextProvider) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                runCatching {
                    getSignedInUserUseCase.execute()
                }.fold(
                    {
                        update { it.copy(isUserLoggedIn = true) }
                    },
                    {
                        update { it.copy(isUserLoggedIn = false) }
                    }
                )
            }
        }
    }

    fun onNameUpdated(name: String) {
        viewModelScope.launch {
            update { state ->
                state.copy(name = name, error = null)
            }
        }
    }

    fun onDescriptionUpdated(description: String) {
        viewModelScope.launch {
            update { state ->
                state.copy(description = description, error = null)
            }
        }
    }

    fun onDueDateSelected(dueDate: LocalDateTime) {
        viewModelScope.launch {
            update { state ->
                state.copy(
                    dueDate = dueDate,
                    formattedDueDate = getFormattedDueDate.execute(dueDate),
                    error = null
                )
            }
        }
    }

    fun onPrioritySelected(priority: TaskPriority) {
        viewModelScope.launch {
            update { state ->
                state.copy(priority = priority, error = null)
            }
        }
    }

    fun onAttendeeAdded(value: String) {
        viewModelScope.launch {
            update { state ->
                state.copy(
                    attendees = mutableSetOf<String>().apply {
                        addAll(currentState().attendees)
                        add(value)
                    }
                )
            }
        }
    }

    fun onAttendeeRemoved(value: String) {
        viewModelScope.launch {
            update { state ->
                val attendees = currentState().attendees.toMutableSet()
                attendees.remove(value)
                state.copy(attendees = attendees)
            }
        }
    }

    fun onDonePressed() {
        viewModelScope.launch {
            update {
                it.copy(
                    loading = true,
                    error = null
                )
            }

            runCatching {
                withContext(coroutineContextProvider.io) {
                    val currentState = currentState()
                    addTaskUseCase.execute(
                        AddTaskUseCase.AddTaskParam(
                            name = currentState.name,
                            description = currentState.description,
                            dueDate = currentState.dueDate,
                            priority = currentState.priority,
                            attendees = if (currentState.attendees.isEmpty()) {
                                null
                            } else {
                                currentState.attendees
                            }
                        )
                    )
                }
            }.fold(
                {
                    _event.tryEmit(Event.TaskAdded)
                },
                { error ->
                    if (error !is AddTaskException) {
                        throw error
                    }

                    onParamsValidationError(error)
                }
            )
        }
    }

    private suspend fun onParamsValidationError(error: AddTaskException) {
        update {
            it.copy(
                loading = false,
                error = stringProvider.get(
                    when (error) {
                        is AddTaskException.MissingName ->
                            R.string.name_validation_error
                        is AddTaskException.MissingPriority ->
                            R.string.prioriority_validation_error
                        is AddTaskException.AttendeesWrongFormat ->
                            R.string.attendees_validation_error
                        is AddTaskException.AttendeesRequiredDueDate ->
                            R.string.attendees_without_due_date_error
                    }
                )
            )
        }
    }

    data class ViewState(
        val loading: Boolean = false,
        val name: String? = null,
        val description: String? = null,
        val dueDate: LocalDateTime? = null,
        val formattedDueDate: String? = null,
        val priority: TaskPriority? = null,
        val isUserLoggedIn: Boolean = false,
        val attendees: Set<String> = setOf(),
        val addtaskScreenConfiguration: AddtaskScreenConfiguration? = null,
        val error: String? = null
    )

    sealed class Event {

        object TaskAdded : Event()
    }
}
