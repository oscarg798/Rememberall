package com.oscarg798.remembrall.addtask.ui

import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintSet
import com.oscarg798.remembrall.addtask.AddTaskViewModel
import com.oscarg798.remembrall.common.extensions.AlignToStart
import com.oscarg798.remembrall.common.extensions.horizontalToParent
import com.oscarg798.remembrall.common.ui.AddEditTaskForm
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun AddTaskForm(
    state: AddTaskViewModel.ViewState,
    viewModel: AddTaskViewModel
) {

    AddEditTaskForm(
        taskName = state.name ?: MissingFieldValuePlaceholder,
        taskDescription = state.description ?: MissingFieldValuePlaceholder,
        availablePriorities = state.addtaskScreenConfiguration?.availablePriorities,
        selectedPriority = state.addtaskScreenConfiguration?.selectedPriority,
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

private fun getConstraints(isUserLoggedIn: Boolean) = ConstraintSet {
    val taskName = createRefFor(TaskNameId)
    val taskDescription = createRefFor(TaskDescriptionId)
    val taskDue = createRefFor(TaskDueDateId)
    val taskPriority = createRefFor(TaskPriorityId)
    val doneButton = createRefFor(DoneButtonId)
    val attendees = createRefFor(AttendeesId)
    val barrier = createBottomBarrier(taskPriority, attendees)

    constrain(taskName) {
        top.linkTo(parent.top, margin = Dimensions.Spacing.Medium)
        horizontalToParent(AlignToStart)
    }

    constrain(taskDescription) {
        top.linkTo(taskName.bottom, margin = Dimensions.Spacing.Medium)
        horizontalToParent(AlignToStart)
    }

    constrain(taskDue) {
        top.linkTo(taskDescription.bottom, margin = Dimensions.Spacing.Medium)
        horizontalToParent(AlignToStart)
    }

    constrain(taskPriority) {
        top.linkTo(taskDue.bottom, margin = Dimensions.Spacing.Medium)
        horizontalToParent(AlignToStart)
    }

    constrain(attendees) {
        top.linkTo(taskPriority.bottom, margin = Dimensions.Spacing.Medium)
        horizontalToParent(AlignToStart)
    }

    constrain(doneButton) {
        linkTo(top = barrier, bottom = parent.bottom, bias = 1f)
        horizontalToParent()
    }
}

private const val DueDatePlaceholder = "Thu, May 14 12:00 mm"
private const val MissingFieldValuePlaceholder = ""

internal const val TaskNameId = "taskName"
const val TaskDescriptionId = "taskDescription"
const val TaskDueDateId = "taskDue"
const val TaskPriorityId = "taskPriority"
const val DoneButtonId = "done"
const val AttendeesId = "Attendees"
