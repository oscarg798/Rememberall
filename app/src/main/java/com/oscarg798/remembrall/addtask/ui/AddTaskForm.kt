package com.oscarg798.remembrall.addtask.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.oscarg798.remembrall.AlignToStart
import com.oscarg798.remembrall.addtask.AddTaskViewModel
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.horizontalToParent

@Composable
internal fun AddTaskForm(
    state: AddTaskViewModel.ViewState,
    viewModel: AddTaskViewModel
) {

    Box(
        modifier = Modifier
            .padding(Dimensions.Spacing.Medium)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight(),
            constraintSet = getConstraints(state.isUserLoggedIn)
        ) {

            TaskNameField(
                name = state.name ?: MissingFieldValuePlaceholder,
                enabled = !state.loading,
            ) { viewModel.onNameUpdated(it) }

            TaskDescriptionField(
                description = state.description ?: MissingFieldValuePlaceholder,
                enabled = !state.loading

            ) { viewModel.onDescriptionUpdated(it) }

            TaskDueDateField(
                formattedDueDate = state.formattedDueDate ?: DueDatePlaceholder,
                enabled = !state.loading,
                isUserLoggedIn = state.isUserLoggedIn,
            ) { viewModel.onDueDateSelected(it) }

            if (state.isUserLoggedIn) {
                AddAttendees(
                    enabled = !state.loading,
                    attendees = state.attendees,
                    onAttendeeRemoved = {
                        viewModel.onAttendeeRemoved(it)
                    },
                    onAttendeeAdded = { value ->
                        viewModel.onAttendeeAdded(value)
                    }
                )
            }

            if (state.addTaskScreenConfiguration != null) {
                TaskPriorityField(
                    availablePriorities = state.addTaskScreenConfiguration.availablePriorities,
                    enabled = !state.loading,
                    selectedPriority = state.priority
                        ?: state.addTaskScreenConfiguration.selectedPriority
                ) {
                    viewModel.onPrioritySelected(
                        it
                    )
                }
            }

            if (state.loading) {
                LoadingDoneButton()
            } else {
                DoneButton {
                    viewModel.onDonePressed()
                }
            }
        }
    }
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
