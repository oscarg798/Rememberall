package com.oscarg798.remembrall.addtask.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.addtask.domain.DueDate
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.task.TaskPriority
import com.oscarg798.remembrall.taskpriorityextensions.getLabel
import com.oscarg798.remembrall.ui.icons.R as IconsR
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui.theming.colorScheme
import com.oscarg798.remembrall.ui.theming.dimensions
import com.oscarg798.remembrall.ui.theming.typo
import com.oscarg798.remembrall.uicolor.SecondaryTextColor
import java.time.LocalDateTime

@Composable
internal fun AddTaskForm(
    modifier: Modifier,
    title: String,
    description: String,
    selectingTaskPriority: Boolean,
    availableTaskPriorities: List<TaskPriority>,
    hasAttendees: Boolean,
    dueDate: DueDate? = null,
    selectedPriority: TaskPriority? = null,
    enabled: Boolean = true,
    onEvent: (Event) -> Unit
) {
    ConstraintLayout(modifier) {
        val (textFields, actionRow) = createRefs()
        val titleStyle = MaterialTheme.typo.h4.copy(fontWeight = FontWeight.SemiBold)
        Column(
            Modifier.constrainAs(textFields) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(actionRow.top)
                height = Dimension.fillToConstraints
            },
        ) {
            TextField(
                value = title,
                onValueChange = { onEvent(Event.OnTitleChanged(it)) },
                enabled = enabled,
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                placeholder = {
                    Text(
                        stringResource(R.string.title_hint),
                        style = titleStyle
                    )
                },
                maxLines = TitleMaxLines,
                textStyle = titleStyle,
                colors = TextFieldColors
            )

            dueDate?.let {
                Text(
                    text = dueDate.displayableDate,
                    modifier = Modifier
                        .padding(start = MaterialTheme.dimensions.Medium),
                    style = MaterialTheme.typo.caption.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        color = SecondaryTextColor
                    ),
                    textAlign = TextAlign.Start,
                    maxLines = DueDateMaxLines
                )
            }

            TextField(
                value = description,
                onValueChange = { onEvent(Event.OnDescriptionChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typo.body1,
                enabled = enabled,
                placeholder = {
                    Text(
                        stringResource(R.string.description_hint),
                        style = MaterialTheme.typo.body1
                    )
                },
                colors = TextFieldColors
            )
        }

        ActionsRow(
            modifier = Modifier.constrainAs(actionRow) {
                linkTo(parent.start, parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            selectingTaskPriority = selectingTaskPriority,
            taskPriorities = availableTaskPriorities,
            selectedPriority = selectedPriority,
            hasDueDateSelected = dueDate != null,
            hasAttendees = hasAttendees,
            enabled = enabled,
            onEvent = onEvent
        )
    }
}

@Composable
private fun ActionsRow(
    taskPriorities: List<TaskPriority>,
    selectingTaskPriority: Boolean,
    selectedPriority: TaskPriority?,
    hasDueDateSelected: Boolean,
    hasAttendees: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onEvent: (Event) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        ActionButton(
            enabled = enabled,
            icon = IconsR.drawable.ic_calendar,
            tintColor = if (hasDueDateSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            onLongClicked = { onEvent(Event.OnCalendarActionLongClicked) },
            onClick = { onEvent(Event.OnCalendarActionClicked) },
        )

        ActionButtonWithDropDown(
            enabled = enabled,
            icon = IconsR.drawable.ic_tag,
            onClick = { onEvent(Event.OnTagActionClicked) },
            tintColor = if (selectedPriority != null) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            onLongClicked = {
                onEvent(Event.OnTagActionLongClicked)
            },
            dropDownContent = {
                TaskPriorityDropDown(
                    expanded = selectingTaskPriority,
                    priorities = taskPriorities,
                    selectedPriority = selectedPriority,
                    onEvent = onEvent,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        )

        ActionButton(
            enabled = enabled,
            icon = IconsR.drawable.ic_attendees,
            onClick = { onEvent(Event.OnAttendeeActionClicked) },
            tintColor = if (hasAttendees) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        ActionButton(
            enabled = enabled,
            icon = IconsR.drawable.ic_save,
            tintColor = MaterialTheme.colorScheme.primary,
            onClick = { onEvent(Event.OnSaveActionClicked) },
        )
    }
}

@Composable
private fun ActionButton(
    @DrawableRes icon: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    tintColor: Color = MaterialTheme.colorScheme.onSurface,
    onLongClicked: () -> Unit = {},
    onClick: () -> Unit
) {
    ActionButtonContainer(
        modifier = modifier,
        enabled = enabled,
        onLongClicked = onLongClicked,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Select a date for the note",
            tint = tintColor
        )
    }
}

@Composable
private fun ActionButtonWithDropDown(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    dropDownContent: @Composable () -> Unit,
    tintColor: Color = MaterialTheme.colorScheme.onSurface,
    onLongClicked: () -> Unit = {},
    onClick: () -> Unit
) {
    ActionButtonContainer(
        modifier = modifier,
        enabled = enabled,
        onLongClicked = onLongClicked,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Select a date for the note",
            tint = tintColor
        )
        dropDownContent()
    }
}

@Composable
private fun ActionButtonContainer(
    modifier: Modifier,
    enabled: Boolean,
    onLongClicked: () -> Unit,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .clip(CircleShape)
            .minimumInteractiveComponentSize()
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 24.dp),
                onClick = onClick,
                enabled = enabled,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClicked()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun TaskPriorityDropDown(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    priorities: List<TaskPriority>,
    selectedPriority: TaskPriority? = null,
    onEvent: (Event) -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = { onEvent(Event.OnTaskPrioritySelectorDismissed) }
    ) {

        priorities.map { priority ->
            key(priority) {
                DropdownMenuItem(
                    onClick = { onEvent(Event.OnPriorityChanged(priority)) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = stringResource(id = priority.getLabel()),
                        style = MaterialTheme.typo.body2.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedPriority == priority) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(device = Devices.NEXUS_5)
private fun AddTaskPreview() {
    val title = remember { mutableStateOf("Watch chelsea Match") }
    val description = remember { mutableStateOf("Why tf R they looking back when they should be ") }
    val priorityBoxExpanded = remember { mutableStateOf(false) }
    val selectedPriority = remember { mutableStateOf<TaskPriority?>(null) }
    RemembrallTheme {
        AddTaskForm(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            title = title.value,
            description = description.value,
            availableTaskPriorities = listOf(
                TaskPriority.High,
                TaskPriority.Medium,
                TaskPriority.Low
            ),
            selectingTaskPriority = priorityBoxExpanded.value,
            selectedPriority = selectedPriority.value,
            dueDate = DueDate(
                LocalDateTime.now(),
                "Mon, Sep 25 2023"
            ),
            hasAttendees = true,
            onEvent = {
                when {
                    it is Event.OnTitleChanged -> title.value = it.title
                    it is Event.OnDescriptionChanged -> description.value = it.description
                    it is Event.OnTagActionClicked && !priorityBoxExpanded.value ->
                        priorityBoxExpanded.value = true

                    it is Event.OnTaskPrioritySelectorDismissed && priorityBoxExpanded.value ->
                        priorityBoxExpanded.value = false

                    it is Event.OnPriorityChanged -> {
                        selectedPriority.value = it.priority
                        priorityBoxExpanded.value = false
                    }
                }
            }
        )
    }
}

internal val TextFieldColors: androidx.compose.material.TextFieldColors
    @Composable
    get() = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = SecondaryTextColor,
        backgroundColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.onSurface,
        errorCursorColor = MaterialTheme.colorScheme.onError,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.DarkGray,
        errorIndicatorColor = MaterialTheme.colorScheme.onError,
        placeholderColor = SecondaryTextColor,
        disabledPlaceholderColor = SecondaryTextColor,
        focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )

private const val DueDateMaxLines = 2
private const val TitleMaxLines = 4
