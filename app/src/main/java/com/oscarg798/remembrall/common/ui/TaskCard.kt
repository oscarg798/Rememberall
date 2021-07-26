package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.common.extensions.getColor
import com.oscarg798.remembrall.common.extensions.getLabel
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.common.ui.theming.SecondaryTextColor

@Composable
fun TaskCard(
    task: DisplayableTask,
    modifier: Modifier,
    onClick: (DisplayableTask) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(Dimensions.CornerRadius.Medium),
        modifier = modifier
            .clickable { onClick(task) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Dimensions.Spacing.Medium)
        ) {
            content()
        }
    }
}

@Composable
fun TaskBody(
    task: DisplayableTask,
    descriptionMaxLines: Int = TaskDescriptionMaxLines,
    showAttendees: Boolean = false,
    taskCardOptions: TaskCardOptions
) {

    val showingDropdown = remember { mutableStateOf(false) }

    TaskHeader(task = task)

    Row {
        TaskTitle(
            task = task,
            modifier = Modifier.weight(
                when (taskCardOptions) {
                    is TaskCardOptions.Present -> .8f
                    else -> 1f
                }
            )
        )

        if (taskCardOptions is TaskCardOptions.Present) {
            Box(
                modifier = Modifier.weight(.2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(SecondaryTextColor),
                    modifier = Modifier
                        .size(
                            OptionsSize
                        )
                        .clickable {
                            showingDropdown.value = true
                        }

                )

                DropdownMenu(
                    expanded = showingDropdown.value,
                    onDismissRequest = { showingDropdown.value = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            taskCardOptions.onRemoveClicked()
                            showingDropdown.value = false
                        }
                    ) {
                        Text(stringResource(R.string.remove_task_label))
                    }
                }
            }
        }
    }

    TaskDescription(task = task, maxLines = descriptionMaxLines)

    if (showAttendees) {
        Attendees(task = task)
    }

    TaskDueDate(task = task)
}

@Composable
private fun Attendees(task: DisplayableTask) {
    val attendees = task.attendees ?: return

    Text(
        text = "Attendees:",
        style = MaterialTheme.typography.h3
            .merge(
                TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontStyle = FontStyle.Italic
                )
            )
    )
    LazyColumn {
        items(attendees.toList()) { attendee ->
            Text(
                text = attendee.email,
                style = TextStyle(color = MaterialTheme.colors.onSurface),
                modifier = Modifier
                    .padding(Dimensions.Spacing.Medium)
            )
        }
    }
}

@Composable
private fun TaskHeader(task: DisplayableTask) {

    Text(
        text = stringResource(task.priority.getLabel()),
        style = MaterialTheme.typography.body1.merge(
            TextStyle(
                color = task.priority.getColor()
            )
        )
    )

    Divider(
        color = SecondaryTextColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimensions.Spacing.ExtraSmall)
            .height(HorizontalDividerHeight)
    )
}

@Composable
fun TaskTitle(task: DisplayableTask, modifier: Modifier = Modifier) {
    Text(
        text = task.name,
        style = MaterialTheme.typography.h3
            .merge(TextStyle(color = MaterialTheme.colors.onSurface)),
        maxLines = SingleLine,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
private fun TaskDescription(task: DisplayableTask, maxLines: Int = TaskDescriptionMaxLines) {
    val description = task.description ?: return
    Text(
        text = description,
        style = MaterialTheme.typography.body1
            .merge(TextStyle(color = SecondaryTextColor)),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun TaskDueDate(task: DisplayableTask) {
    val dueDate = task.dueDate ?: return

    Row(
        modifier = Modifier
            .padding(top = Dimensions.Spacing.ExtraSmall)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription = "",
            colorFilter = ColorFilter.tint(task.getDueDateColor()),
            modifier = Modifier.size(TaskDueIconSize)
        )

        Text(
            text = dueDate,
            style = MaterialTheme.typography.body2
                .merge(TextStyle(color = task.getDueDateColor())),
            maxLines = SingleLine,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = Dimensions.Spacing.Medium)
        )
    }
}

@Composable
private fun DisplayableTask.getDueDateColor() = if (this.calendarSynced) {
    MaterialTheme.colors.secondary
} else {
    SecondaryTextColor
}

sealed interface TaskCardOptions {

    object None : TaskCardOptions
    class Present(val onRemoveClicked: () -> Unit) : TaskCardOptions
}

private const val TaskDescriptionMaxLines = 2
private val HorizontalDividerHeight = 1.dp
private val TaskDueIconSize = 20.dp
private val OptionsSize = 30.dp
