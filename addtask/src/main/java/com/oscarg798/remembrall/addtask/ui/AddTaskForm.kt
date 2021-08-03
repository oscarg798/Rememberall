package com.oscarg798.remembrall.addtask.ui

import androidx.compose.runtime.Composable
import com.oscarg798.remembrall.addtask.AddTaskViewModel
import com.oscarg798.remembrall.common_addedit.ui.AddEditTaskForm

@Composable
internal fun AddTaskForm(
    state: AddTaskViewModel.ViewState,
    viewModel: AddTaskViewModel
) {

    AddEditTaskForm(
        taskName = state.name ?: MissingFieldValuePlaceholder,
        taskDescription = state.description ?: MissingFieldValuePlaceholder,
        availablePriorities = state.addtaskScreenConfiguration?.availablePriorities,
        selectedPriority = state.priority,
        dueDate = when (state.dueDate) {
            null -> null
            else -> state.formattedDueDate
        },
        loading = state.loading,
        isUserLoggedIn = state.isUserLoggedIn,
        onNameUpdated = {
            viewModel.onNameUpdated(it)
        },
        onDescriptionUpdated = {
            viewModel.onDescriptionUpdated(it)
        },
        onDueDateSelected = {
            viewModel.onDueDateSelected(it)
        },
        onAttendeeAdded = {
            viewModel.onAttendeeAdded(it)
        },
        onAttendeeRemoved = {
            viewModel.onAttendeeRemoved(it)
        },
        onPrioritySelected = {
            viewModel.onPrioritySelected(it)
        },
        onDonePressed = {
            viewModel.onDonePressed()
        }
    )
}

private const val MissingFieldValuePlaceholder = ""
