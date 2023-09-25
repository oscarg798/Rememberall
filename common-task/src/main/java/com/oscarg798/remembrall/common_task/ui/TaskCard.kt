package com.oscarg798.remembrall.common_task.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
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
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.ui_common.R
import com.oscarg798.remembrall.ui_common.extensions.getColor
import com.oscarg798.remembrall.ui_common.extensions.getLabel
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.SecondaryTextColor

@Deprecated("This must be elsewhere")
@Composable
fun TaskCard(
    task: DisplayableTask,
    modifier: Modifier,
    onClick: (DisplayableTask) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        contentColor = MaterialTheme.colorScheme.surface,
        backgroundColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(RemembrallTheme.dimens.Medium),
        modifier = modifier
            .clickable { onClick(task) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(RemembrallTheme.dimens.Medium)
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
    taskCardOptions: TaskCardOptions,
    onOptionClicked: (DisplayableTask, TaskCardOptions.Option) -> Unit = { _, _ -> }
) {

    val showingDropdown = remember { mutableStateOf(false) }

    TaskHeader(task = task)

    Row {
        TaskTitle(
            task = task,
            modifier = Modifier
                .padding(top = RemembrallTheme.dimens.Small)
                .weight(
                    when {
                        taskCardOptions is TaskCardOptions.Present && task.owned -> .8f
                        else -> 1f
                    }
                )
        )

        if (taskCardOptions is TaskCardOptions.Present && task.owned) {
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
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    expanded = showingDropdown.value,
                    onDismissRequest = { showingDropdown.value = false }
                ) {
                    taskCardOptions.options.map {
                        DropdownMenuItem(
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            onClick = {
                                onOptionClicked(task, it)
                                showingDropdown.value = false
                            }
                        ) {
                            Text(
                                stringResource(it.title),
                                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                            )
                        }
                    }
                }
            }
        }
    }

    TaskDescription(task = task, maxLines = descriptionMaxLines)

    if (showAttendees && task.attendees?.isEmpty() == false) {
        Attendees(attendees = task.attendees!!)
    }

    Row(Modifier.fillMaxWidth()) {
        task.dueDate?.let {
            TaskDueDate(
                dueDate = it,
                Modifier
                    .padding(top = RemembrallTheme.dimens.ExtraSmall)
                    .weight(if (task.owned) 1f else .8f)
            )
        }


        if (!task.owned) {
            Image(
                painter = painterResource(id = com.oscarg798.remembrall.common_task.R.drawable.ic_attendee),
                contentDescription = "attendee indicator",
                modifier = Modifier
                    .size(20.dp)
                    .weight(.2f)
            )
        }
    }
}

@Composable
private fun Attendees(attendees: Collection<CalendarAttendee>) {
    Text(
        text = "Attendees:",
        style = MaterialTheme.typography.displaySmall
            .merge(
                TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontStyle = FontStyle.Italic
                )
            )
    )
    LazyColumn {
        items(attendees.toList()) { attendee ->
            Text(
                text = attendee.email,
                style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .padding(RemembrallTheme.dimens.Medium)
            )
        }
    }
}

@Composable
private fun TaskHeader(task: DisplayableTask) {
    task.priority?.let {
        Text(
            text = stringResource(it.getLabel()),
            style = MaterialTheme.typography.bodySmall.merge(
                TextStyle(
                    color = it.getColor()
                )
            )
        )
    }


    Divider(
        color = SecondaryTextColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = RemembrallTheme.dimens.ExtraSmall)
            .height(HorizontalDividerHeight)
    )
}

@Composable
fun TaskTitle(task: DisplayableTask, modifier: Modifier = Modifier) {
    Text(
        text = task.name,
        style = MaterialTheme.typography.titleMedium
            .merge(TextStyle(color = MaterialTheme.colorScheme.onSurface)),
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
        style = MaterialTheme.typography.bodySmall
            .merge(TextStyle(color = SecondaryTextColor)),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun TaskDueDate(dueDate: String, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
            modifier = Modifier.size(TaskDueIconSize)
        )

        Text(
            text = dueDate,
            style = MaterialTheme.typography.labelMedium
                .merge(TextStyle(color = MaterialTheme.colorScheme.secondary)),
            maxLines = SingleLine,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = RemembrallTheme.dimens.Medium)
        )
    }
}


sealed interface TaskCardOptions {

    object None : TaskCardOptions
    data class Present(val options: List<Option>) : TaskCardOptions

    sealed class Option(
        @StringRes
        val title: Int
    ) {
        object Remove : Option(R.string.remove_task_label)
        object Edit : Option(R.string.edit_task_label)
    }
}

private const val TaskDescriptionMaxLines = 2
private val HorizontalDividerHeight = 1.dp
private val TaskDueIconSize = 20.dp
private val OptionsSize = 30.dp
