package com.oscarg798.remembrall.edittask

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.formatters.DueDateFormatter
import com.oscarg798.remembrall.common.model.CalendarAttendee
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.model.EditableTask
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.usecase.GetSignedInUserUseCase
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTaskViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    private val getEditableTaskUseCase: GetEditableTaskUseCase,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val dueDateFormatter: DueDateFormatter,
    private val editTaskUseCase: EditTaskUseCase,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<EditTaskViewModel.ViewState, EditTaskViewModel.Event>(
    ViewState(),
    coroutineContextProvider
) {

    init {
        viewModelScope.launch {
            async {
                getTaskToEdit()
            }
            async {
                getUserSessionState()
            }
        }
    }

    private suspend fun getUserSessionState() {
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

    private suspend fun getTaskToEdit() {
        val editableTask = withContext(coroutineContextProvider.io) {
            getEditableTaskUseCase.execute(taskId)
        }

        update {
            it.copy(
                loading = false,
                editableTask = editableTask
            )
        }
    }

    @AssistedFactory
    interface EditTaskViewModelFactory {

        fun create(taskId: String): EditTaskViewModel
    }

    fun onNameUpdated(name: String) {
        viewModelScope.launch {
            update {
                val currentEditableTask = it.editableTask
                    ?: throw IllegalStateException("Edit is not allowed on non editable state")
                val updatedTask = currentEditableTask.task.copy(name = name)

                it.copy(
                    loading = false,
                    editableTask = currentEditableTask.copy(
                        task = updatedTask,
                        displayableTask = DisplayableTask(
                            task = updatedTask,
                            dueDateFormatter = dueDateFormatter
                        )
                    )
                )
            }
        }
    }

    fun onDescriptionUpdated(description: String) {
        viewModelScope.launch {
            update {
                val currentEditableTask = it.editableTask
                    ?: throw IllegalStateException("Edit is not allowed on non editable state")
                val updatedTask = currentEditableTask.task.copy(description = description)

                it.copy(
                    loading = false,
                    editableTask = currentEditableTask.copy(
                        task = updatedTask,
                        displayableTask = DisplayableTask(
                            task = updatedTask,
                            dueDateFormatter = dueDateFormatter
                        )
                    )
                )
            }
        }
    }

    fun onAttendeeAdded(attendeeEmail: String) {
        viewModelScope.launch {
            update {
                val currentEditableTask = it.editableTask
                    ?: throw IllegalStateException("Edit is not allowed on non editable state")

                val currentAttendees = currentEditableTask.task.calendarSyncInformation?.attendees
                    ?: currentEditableTask.attendees

                val attendees = mutableSetOf<CalendarAttendee>().apply {
                    addAll(currentAttendees)
                    add(CalendarAttendee(attendeeEmail))
                }

                if (currentEditableTask.task.calendarSyncInformation != null) {
                    val updatedTask = currentEditableTask.task.copy(
                        calendarSyncInformation = currentEditableTask
                            .task.calendarSyncInformation.copy(
                                attendees = attendees
                            )
                    )
                    it.copy(
                        loading = false,
                        editableTask = currentEditableTask.copy(
                            task = updatedTask,
                            displayableTask = DisplayableTask(
                                task = updatedTask,
                                dueDateFormatter = dueDateFormatter
                            )
                        )
                    )
                } else {
                    it.copy(
                        loading = false,
                        editableTask = currentEditableTask.copy(attendees = attendees)
                    )
                }
            }
        }
    }

    fun onAttendeeRemoved(attendeeEmail: String) {
        viewModelScope.launch {
            update {
                val currentEditableTask = it.editableTask
                    ?: throw IllegalStateException("Edit is not allowed on non editable state")

                val attendees =
                    currentEditableTask.task.calendarSyncInformation?.attendees
                        ?: currentEditableTask.attendees

                val updatedAttendees = attendees.toMutableSet()
                updatedAttendees.remove(CalendarAttendee(attendeeEmail))

                if (currentEditableTask.task.calendarSyncInformation != null) {
                    val updatedTask = currentEditableTask.task.copy(
                        calendarSyncInformation = currentEditableTask
                            .task.calendarSyncInformation.copy(
                                attendees = updatedAttendees
                            )
                    )
                    it.copy(
                        loading = false,
                        editableTask = currentEditableTask.copy(
                            task = updatedTask,
                            displayableTask = DisplayableTask(
                                task = updatedTask,
                                dueDateFormatter = dueDateFormatter
                            )
                        )
                    )
                } else {
                    it.copy(
                        loading = false,
                        editableTask = currentEditableTask.copy(attendees = updatedAttendees)
                    )
                }
            }
        }
    }

    fun onDueDateSelected(dueDate: LocalDateTime) {
        viewModelScope.launch {

            update {

                val currentEditableTask = currentState().editableTask
                    ?: throw IllegalStateException("Edit is not allowed on non editable state")

                val updatedTask = currentEditableTask.task.copy(
                    dueDate = dueDateFormatter.toDueDateInMillis(dueDate)
                )

                it.copy(
                    loading = false,
                    editableTask = currentEditableTask.copy(
                        task = updatedTask,
                        displayableTask = DisplayableTask(
                            task = updatedTask,
                            dueDateFormatter = dueDateFormatter
                        )
                    )
                )
            }
        }
    }

    fun onPrioritySelected(priority: TaskPriority) {
        viewModelScope.launch {

            update {

                val currentEditableTask = currentState().editableTask
                    ?: throw IllegalStateException("Edit is not allowed on non editable state")

                val updatedTask = currentEditableTask.task.copy(priority = priority)

                it.copy(
                    loading = false,
                    editableTask = currentEditableTask.copy(
                        task = updatedTask,
                        displayableTask = DisplayableTask(
                            task = updatedTask,
                            dueDateFormatter = dueDateFormatter
                        )
                    )
                )
            }
        }
    }

    fun onDonePressed() {
        viewModelScope.launch {
            update { it.copy(loading = true) }

            val editableTask = currentState().editableTask
                ?: throw IllegalStateException("Done can not be pressed without editable task")

            runCatching {
                withContext(coroutineContextProvider.io) {
                    editTaskUseCase.execute(editableTask)
                }
            }.fold(
                {
                    _event.tryEmit(Event.TaskEdited)
                },
                { error ->
                    if (error !is Exception) throw error

                    update { it.copy(loading = false, error = error) }
                }
            )
        }
    }

    data class ViewState(
        val loading: Boolean = false,
        val editableTask: EditableTask? = null,
        val isUserLoggedIn: Boolean = false,
        val error: Exception? = null
    )

    sealed interface Event {
        object TaskEdited : Event
    }
}
