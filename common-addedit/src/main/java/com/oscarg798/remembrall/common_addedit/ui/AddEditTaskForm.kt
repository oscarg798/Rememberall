package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.oscarg798.remembrall.task.TaskPriority
import com.oscarg798.remembrall.common_addedit.R
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import java.time.LocalDateTime

@Composable
fun AddEditTaskForm(
    taskName: String,
    taskDescription: String?,
    dueDate: String? = null,
    attendees: Set<String>? = emptySet(),
    availablePriorities: List<TaskPriority>?,
    selectedPriority: TaskPriority?,
    loading: Boolean,
    onNameUpdated: (String) -> Unit,
    onDescriptionUpdated: (String) -> Unit,
    onDueDateSelected: (LocalDateTime) -> Unit,
    onAttendeeAdded: (String) -> Unit,
    onAttendeeRemoved: (String) -> Unit,
    onPrioritySelected: (TaskPriority) -> Unit,
    onDonePressed: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(RemembrallTheme.dimens.Medium)) {
        TaskNameField(name = taskName, enabled = !loading) { onNameUpdated(it) }

        TaskDescriptionField(
            description = taskDescription ?: "",
            enabled = !loading

        ) { onDescriptionUpdated(it) }

        TaskDueDateField(
            formattedDueDate = dueDate ?: DueDatePlaceholder,
            enabled = !loading,
        ) { onDueDateSelected(it) }

        if (dueDate != null) {
            AddAttendees(
                enabled = !loading,
                attendees = attendees,
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
