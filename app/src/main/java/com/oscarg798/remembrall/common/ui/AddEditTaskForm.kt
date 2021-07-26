package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.addtask.ui.AddAttendees
import com.oscarg798.remembrall.addtask.ui.RemembrallButton
import com.oscarg798.remembrall.addtask.ui.TaskDescriptionField
import com.oscarg798.remembrall.addtask.ui.TaskDueDateField
import com.oscarg798.remembrall.addtask.ui.TaskNameField
import com.oscarg798.remembrall.addtask.ui.TaskPriorityField
import com.oscarg798.remembrall.common.model.CalendarAttendee
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import java.time.LocalDateTime

@Composable
fun AddEditTaskForm(
    taskName: String,
    taskDescription: String?,
    dueDate: String? = null,
    attendees: Set<CalendarAttendee> = emptySet(),
    availablePriorities: List<TaskPriority>?,
    selectedPriority: TaskPriority?,
    loading: Boolean,
    isUserLoggedIn: Boolean,
    onNameUpdated: (String) -> Unit,
    onDescriptionUpdated: (String) -> Unit,
    onDueDateSelected: (LocalDateTime) -> Unit,
    onAttendeeAdded: (String) -> Unit,
    onAttendeeRemoved: (String) -> Unit,
    onPrioritySelected: (TaskPriority) -> Unit,
    onDonePressed: () -> Unit
) {
    Column(Modifier.padding(Dimensions.Spacing.Medium)) {
        TaskNameField(
            name = taskName,
            enabled = !loading,
        ) { onNameUpdated(it) }

        TaskDescriptionField(
            description = taskDescription ?: "",
            enabled = !loading

        ) { onDescriptionUpdated(it) }

        TaskDueDateField(
            formattedDueDate = dueDate ?: DueDatePlaceholder,
            enabled = !loading,
            isUserLoggedIn = isUserLoggedIn,
        ) { onDueDateSelected(it) }

        if (isUserLoggedIn && dueDate != null) {
            AddAttendees(
                enabled = !loading,
                attendees = attendees.map {
                    it.email
                }.toSet(),
                onAttendeeRemoved = {
                    onAttendeeRemoved(it)
                },
                onAttendeeAdded = { value ->
                    onAttendeeAdded(value)
                }
            )
        }

        if (availablePriorities != null) {
            TaskPriorityField(
                availablePriorities = availablePriorities,
                enabled = !loading,
                selectedPriority = selectedPriority
            ) {
                onPrioritySelected(it)
            }
        }

        RemembrallButton(
            text = stringResource(R.string.done_label),
            loading = loading
        ) {
            onDonePressed()
        }
    }
}

private const val DueDatePlaceholder = "Thu, May 14 12:00 mm"
