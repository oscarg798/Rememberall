package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.AlignToStart
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.common.extensions.getColor
import com.oscarg798.remembrall.common.extensions.getLabel
import com.oscarg798.remembrall.common.extensions.horizontalToParent
import com.oscarg798.remembrall.common.extensions.toParentEnd
import com.oscarg798.remembrall.common.extensions.toParentStart
import com.oscarg798.remembrall.common.extensions.toParentTop
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.common.ui.theming.SecondaryTextColor
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTask

@Composable
internal fun TaskItem(
    task: DisplayableTask,
    onRemoveClicked: () -> Unit
) {

    val showingDropdown = remember { mutableStateOf(false) }
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(Dimensions.CornerRadius.Medium),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = Dimensions.Spacing.Small,
                horizontal = Dimensions.Spacing.Medium
            )
    ) {

        ConstraintLayout(
            constraintSet = getConstraints(task),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Dimensions.Spacing.Medium)

        ) {
            Text(
                text = stringResource(task.priority.getLabel()),
                style = MaterialTheme.typography.body1.merge(
                    TextStyle(
                        color = task.priority.getColor()
                    )
                ),
                modifier = Modifier.layoutId(PriorityId)
            )

            Divider(
                color = SecondaryTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.Spacing.ExtraSmall)
                    .height(HorizontalDividerHeight)
                    .layoutId(PriorityHorizontalDividerId)
            )

            Divider(
                color = task.priority.getColor(),
                modifier = Modifier
                    .width(Dimensions.Spacing.ExtraSmall)
                    .padding(top = Dimensions.Spacing.ExtraSmall)
                    .layoutId(PriorityVerticalDividerId)
            )

            Text(
                text = task.name,
                style = MaterialTheme.typography.h3
                    .merge(TextStyle(color = MaterialTheme.colors.onSurface)),
                modifier = Modifier
                    .padding(start = Dimensions.Spacing.Small)
                    .layoutId(TaskTitleId),
                maxLines = SingleLine,
                overflow = TextOverflow.Ellipsis
            )

            if (task.description != null) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.body1
                        .merge(TextStyle(color = SecondaryTextColor)),
                    modifier = Modifier
                        .padding(start = Dimensions.Spacing.Small)
                        .layoutId(TaskDescriptionId),
                    maxLines = TaskDescriptionMaxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_more), contentDescription = "",
                colorFilter = ColorFilter.tint(SecondaryTextColor),
                modifier = Modifier
                    .size(
                        OptionsSize
                    )
                    .clickable {
                        showingDropdown.value = true
                    }
                    .layoutId(OptionsId)
            )

            if (task.dueDate != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_time), contentDescription = "",
                    colorFilter = ColorFilter.tint(task.getDueDateColor()),
                    modifier = Modifier
                        .size(TaskDueIconSize)
                        .layoutId(DueTimeIconId)
                )

                Text(
                    text = task.dueDate,
                    style = MaterialTheme.typography.body2
                        .merge(TextStyle(color = task.getDueDateColor())),
                    modifier = Modifier.layoutId(TaskDueDateId),
                    maxLines = SingleLine,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(modifier = Modifier.layoutId(DropdownMenuId)) {
                DropdownMenu(
                    expanded = showingDropdown.value,
                    onDismissRequest = { showingDropdown.value = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onRemoveClicked()
                            showingDropdown.value = false
                        }
                    ) {
                        Text(stringResource(R.string.remove_task_label))
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayableTask.getDueDateColor() = if (this.calendarSynced) {
    MaterialTheme.colors.secondary
} else {
    SecondaryTextColor
}

@Composable
private fun getConstraints(task: DisplayableTask) = ConstraintSet {
    val priorityId = createRefFor(PriorityId)
    val priorityHorizontalDividerId = createRefFor(PriorityHorizontalDividerId)
    val titleId = createRefFor(TaskTitleId)
    val descriptionId = createRefFor(TaskDescriptionId)
    val dueTimeIconId = createRefFor(DueTimeIconId)
    val dueDate = createRefFor(TaskDueDateId)
    val priorityVerticalDividerId = createRefFor(PriorityVerticalDividerId)
    val optionsId = createRefFor(OptionsId)
    val barrier = createBottomBarrier(titleId, descriptionId)
    val dropDownMenu = createRefFor(DropdownMenuId)

    constrain(priorityId) {
        toParentTop()
        toParentStart()
    }

    constrain(priorityHorizontalDividerId) {
        top.linkTo(priorityId.bottom)
        horizontalToParent()
    }

    constrain(priorityVerticalDividerId) {
        height = Dimension.fillToConstraints
        top.linkTo(titleId.top)
        bottom.linkTo(barrier)
        toParentStart()
    }

    constrain(titleId) {
        top.linkTo(priorityHorizontalDividerId.bottom, margin = Dimensions.Spacing.Small)
        linkTo(
            start = priorityVerticalDividerId.start,
            end = optionsId.start,
            bias = AlignToStart,
            endMargin = Dimensions.Spacing.Medium
        )
    }

    constrain(descriptionId) {
        top.linkTo(titleId.bottom)
        linkTo(start = titleId.start, end = titleId.end, bias = AlignToStart)
    }

    if (task.dueDate != null) {
        constrain(dueTimeIconId) {
            top.linkTo(barrier, Dimensions.Spacing.Medium)
            bottom.linkTo(parent.bottom)
            linkTo(start = titleId.start, end = titleId.end, bias = AlignToStart)
        }

        constrain(dueDate) {
            width = Dimension.fillToConstraints
            top.linkTo(dueTimeIconId.top)
            linkTo(
                start = dueTimeIconId.end,
                end = parent.end,
                bias = AlignToStart,
                startMargin = Dimensions.Spacing.Small
            )
        }
    }

    constrain(optionsId) {
        toParentEnd()
        linkTo(top = titleId.top, bottom = titleId.bottom)
    }

    constrain(dropDownMenu) {
        end.linkTo(parent.end)
    }
}

private const val PriorityId = "priority"
private const val PriorityHorizontalDividerId = "priorityDivider"
private const val TaskTitleId = "taskTitle"
private const val TaskDescriptionId = "taskDescription"
private const val DueTimeIconId = "dueTime"
private const val TaskDueDateId = "taskDueDate"
private const val PriorityVerticalDividerId = "priorityVerticalDivider"
private const val OptionsId = "optionsId"
private const val DropdownMenuId = "dropDownMenu"

private val HorizontalDividerHeight = 1.dp
private val OptionsSize = 30.dp
private const val TaskDescriptionMaxLines = 2
private val TaskDueIconSize = 20.dp
