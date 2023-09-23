@file:OptIn(ExperimentalMaterialApi::class)

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.common.extensions.horizontalToParent
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.ui_common.extensions.getLabel
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme


@Composable
internal fun AddTaskForm(
    modifier: Modifier,
    title: String,
    description: String,
    selectingTaskPriority: Boolean,
    availableTaskPriorities: List<TaskPriority>,
    selectedPriority: TaskPriority? = null,
    onEvent: (Event) -> Unit
) {
    ConstraintLayout(modifier) {
        val (textFields, dateField, actionRow) = createRefs()
        val titleStyle = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold)
        Column(
            Modifier.constrainAs(textFields) {
                horizontalToParent()
                top.linkTo(parent.top)
                bottom.linkTo(dateField.top)
                height = Dimension.fillToConstraints
            },
        ) {
            TextField(
                value = title,
                onValueChange = { Event.OnTitleChanged(it) },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                placeholder = {
                    Text(
                        "Awesome Title",
                        style = titleStyle
                    )
                },
                maxLines = TitleMaxLines,
                textStyle = titleStyle,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    disabledTextColor = Color.Gray,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                    errorCursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Gray,
                    errorIndicatorColor = Color.Gray,
                    placeholderColor = Color.Gray,
                    disabledPlaceholderColor = Color.DarkGray
                )
            )

            TextField(
                value = description,
                onValueChange = { Event.OnTitleChanged(description) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        "Tap here to start...",
                        style = MaterialTheme.typography.body1
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    disabledTextColor = Color.Gray,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                    errorCursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.DarkGray,
                    errorIndicatorColor = Color.Gray,
                    placeholderColor = Color.Gray,
                    disabledPlaceholderColor = Color.Gray
                )
            )
        }

        ActionsRow(
            modifier = Modifier.constrainAs(actionRow) {
                horizontalToParent()
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            selectingTaskPriority = selectingTaskPriority,
            taskPriorities = availableTaskPriorities,
            selectedPriority = selectedPriority,
            onEvent = onEvent
        )
    }
}

@Composable
private fun ActionsRow(
    taskPriorities: List<TaskPriority>,
    selectingTaskPriority: Boolean,
    selectedPriority: TaskPriority?,
    modifier: Modifier = Modifier,
    onEvent: (Event) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = ActionRowBackgroundColor, shape = CircleShape)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        ActionButton(
            icon = R.drawable.ic_calendar,
            onClick = { onEvent(Event.OnCalendarActionClicked) },
        )

        ActionButtonWithDropDown(
            icon = R.drawable.ic_tag,
            onClick = { onEvent(Event.OnTagActionClicked) },
            dropDownContent = {

                TaskPriorityDropDown(
                    expanded = selectingTaskPriority,
                    priorities = taskPriorities,
                    selectedPriority = selectedPriority,
                    onEvent = onEvent,
                    modifier = Modifier.background(ActionRowBackgroundColor)
                )
            }
        )

        ActionButton(
            icon = R.drawable.ic_attendees,
            onClick = { onEvent(Event.OnAttendeeActionClicked) },
        )
    }
}

@Composable
private fun ActionButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(
                color = ActionButtonBackgroundColor,
                shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = "Select a date for the note "
        )
    }
}

@Composable
private fun ActionButtonWithDropDown(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    dropDownContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(
            color = ActionButtonBackgroundColor,
            shape = CircleShape
        )
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = "Select a date for the note "
        )
        dropDownContent()
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
        onDismissRequest = { onEvent(Event.OnTaskPrioritySelectorDismissed) }) {

        priorities.map { priority ->
            key(priority) {
                DropdownMenuItem(
                    onClick = { onEvent(Event.OnPriorityChanged(priority)) },
                    modifier = Modifier.background(ActionRowBackgroundColor)
                ) {
                    Text(
                        text = stringResource(id = priority.getLabel()),
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
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
        AddTaskForm(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6750A4))
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


private val ActionButtonBackgroundColor = Color(0x50FFFFFF)
private val ActionRowBackgroundColor = Color(0x50CCCCCC)
private const val TitleMaxLines = 4