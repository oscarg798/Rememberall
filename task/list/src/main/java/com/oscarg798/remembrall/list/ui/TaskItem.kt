package com.oscarg798.remembrall.list.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card as Card3
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
import com.oscarg798.remembrall.list.domain.model.DisplayableTask
import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.common.R as CommonR
import com.oscarg798.remembrall.taskpriorityextensions.getColor
import com.oscarg798.remembrall.taskpriorityextensions.getLabel
import com.oscarg798.remembrall.ui.extensions.SingleLine
import com.oscarg798.remembrall.ui.icons.R as IconsR
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.dimensions.typo
import com.oscarg798.remembrall.uicolor.SecondaryTextColor
import java.util.UUID
import kotlin.random.Random

@Composable
internal fun TaskItem(
    task: DisplayableTask,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    options: List<TaskCardOptions.Option>,
    onOptionClicked: (DisplayableTask, TaskCardOptions.Option) -> Unit
) {

    TaskCard(
        task = task,
        onClick = {
            onClick(task.id)
        },
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        TaskBody(
            task = task,
            taskCardOptions = TaskCardOptions.Present(
                options
            ),
            onOptionClicked = onOptionClicked
        )
    }
}

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
        shape = RoundedCornerShape(MaterialTheme.dimensions.Medium),
        modifier = modifier
            .clickable { onClick(task) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(MaterialTheme.dimensions.Medium)
        ) {
            content()
        }
    }
}

@Composable
private fun TaskBody(
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
                .padding(top = MaterialTheme.dimensions.Small)
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
                    painter = painterResource(id = IconsR.drawable.ic_more),
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
                    .padding(top = MaterialTheme.dimensions.ExtraSmall)
                    .weight(if (task.owned) 1f else .8f)
            )
        }

        if (!task.owned) {
            Image(
                painter = painterResource(id = IconsR.drawable.ic_attendees),
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
                    .padding(MaterialTheme.dimensions.Medium)
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
            .padding(top = MaterialTheme.dimensions.ExtraSmall)
            .height(HorizontalDividerHeight)
    )
}

@Composable
private fun TaskTitle(task: DisplayableTask, modifier: Modifier = Modifier) {
    Text(
        text = task.title,
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
            painter = painterResource(id = IconsR.drawable.ic_time),
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
            modifier = Modifier.padding(start = MaterialTheme.dimensions.Medium)
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
        object Remove : Option(CommonR.string.remove_task_label)
        object Edit : Option(CommonR.string.edit_task_label)
    }
}

@Composable
private fun TaskItem2(
    task: DisplayableTask,
    modifier: Modifier
) {
    Card3(
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.Medium)
        ) {

            Text(
                text = task.title,
                style = MaterialTheme.typo.h4.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            task.description?.let {
                Text(
                    text = task.description,
                    style = MaterialTheme.typo.body1.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }

            task.dueDate?.let {
                TextIconField(
                    text = it,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun TextIconField(
    text: String,
    modifier: Modifier,
    iconRes: Int? = null,
) {
    ConstraintLayout(modifier) {
        val (textField, icon) = createRefs()
        Text(
            text = text, style = MaterialTheme.typo.caption.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            ), modifier = Modifier.constrainAs(textField) {
                linkTo(parent.top, parent.bottom)
                start.linkTo(parent.start)
                width = Dimension.wrapContent

            }
        )

        iconRes?.let {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "priority icon",
                modifier = Modifier.constrainAs(icon) {
                    linkTo(textField.top, textField.bottom)
                    start.linkTo(textField.end)
                }
            )
        }

    }
}

@Composable
private fun TaskList2(
    tasks: List<DisplayableTask>,
    modifier: Modifier
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.Medium),
        verticalItemSpacing = MaterialTheme.dimensions.Medium
    ) {
        items(tasks, key = { it.id }) {
            TaskItem2(task = it, modifier = Modifier.fillMaxWidth())
        }
    }

}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun TaskListPreview() {
    val data = remember { generateRandomData() }
    com.oscarg798.remembrall.ui.theming.RemembrallTheme {
        TaskList2(
            data,
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

private fun generateRandomData(): List<DisplayableTask> {
    val items = Random.nextInt(5, 20)
    return (0..items).map {
        DisplayableTask(
            id = UUID.randomUUID().toString(),
            owned = Random.nextBoolean(),
            title = LoremIpsum(Random.nextInt(1, 5)).values.joinToString(" "),
            description = LoremIpsum(Random.nextInt(20, 100)).values.joinToString(" "),
            dueDate = if (Random.nextBoolean()) "Sunday 23 Dec, 22" else null
        )
    }

}

@Preview
@Composable
private fun TaskItemPreview() {
    com.oscarg798.remembrall.ui.theming.RemembrallTheme {
        Box {
            TaskItem2(
                task = DisplayableTask(
                    id = "1",
                    owned = true,
                    title = "Call someone",
                    description = """We should be calling this afternoon so we can check all details please do it 
                        otherwise we will need to call tomorrow, and if we do not call tomorrow otherwise 
                        will have to call they day after tomorrow and so on, until we might endup not
                        calling at all. Can you imagine such as tragedy 
          """.trimIndent().replace("\n", ""),
                    dueDate = "Monday 29 Sep, 23"
                ),
                modifier = Modifier,
            )
        }

    }
}


private const val TaskDescriptionMaxLines = 2
private val HorizontalDividerHeight = 1.dp
private val TaskDueIconSize = 20.dp
private val OptionsSize = 30.dp
