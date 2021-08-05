package com.oscarg798.remembrall.edittask

import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.CalendarAttendee
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.edittask.usecase.EditTaskUseCase
import com.oscarg798.remembrall.edittask.usecase.GetEditableTaskUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class EditTaskViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    private val getEditableTaskUseCase: GetEditableTaskUseCase,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val dueDateFormatter: DueDateFormatter,
    private val editTaskUseCase: EditTaskUseCase,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<EditTaskViewModel.ViewState, EditTaskViewModel.Event>(
    ViewState()
), CoroutineContextProvider by coroutineContextProvider {

    init {
        launch {
            getTaskToEdit()
        }
    }

    private suspend fun getTaskToEdit() {
        val editableTask = withContext(io) {
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
        launch {
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
        launch {
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
        launch {
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
                            .task.calendarSyncInformation!!.copy(
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
        launch {
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
                            .task.calendarSyncInformation!!.copy(
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
        launch {

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
        launch {

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
        launch {
            update { it.copy(loading = true) }

            val editableTask = currentState().editableTask
                ?: throw IllegalStateException("Done can not be pressed without editable task")

            runCatching {
                withContext(io) {
                    editTaskUseCase.execute(editableTask)
                }
            }.fold(
                {
                    update { it.copy(loading = false) }
                    _event.tryEmit(Event.TaskEdited)
                },
                { error ->
                    update { it.copy(loading = false) }
                    if (error !is Exception) throw error

                    update { it.copy(loading = false, error = error) }
                }
            )
        }
    }

    data class ViewState(
        val loading: Boolean = false,
        val editableTask: com.oscarg798.remembrall.edittask.model.EditableTask? = null,
        val error: Exception? = null
    )

    sealed interface Event {
        object TaskEdited : Event
    }
}
