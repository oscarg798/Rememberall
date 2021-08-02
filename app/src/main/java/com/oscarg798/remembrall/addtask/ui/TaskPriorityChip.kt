package com.oscarg798.remembrall.addtask.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.AlignToStart
import com.oscarg798.remembrall.common.extensions.getBackgroundColor
import com.oscarg798.remembrall.common.extensions.getColor
import com.oscarg798.remembrall.common.extensions.getLabel
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.ui_common.theming.Dimensions

@Composable
fun TaskPriorityChip(
    priority: TaskPriority,
    isSelected: Boolean = false,
    enabled: Boolean,
    onChipClicked: (TaskPriority) -> Unit
) {
    ConstraintLayout(
        constraintSet = getConstraints(),
        modifier = Modifier.clickable {
            if (enabled) {
                onChipClicked(priority)
            }
        }
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.layoutId(LabelBoxId)
                .padding(Dimensions.Spacing.ExtraSmall)
                .defaultMinSize(minWidth = 75.dp)
                .clip(RoundedCornerShape(Dimensions.CornerRadius.Small))
                .background(priority.getBackgroundColor()).fillMaxWidth()
        ) {
            Text(
                text = stringResource(priority.getLabel()),
                style = MaterialTheme.typography.body1.merge(
                    TextStyle(
                        color = priority.getColor(),
                        fontWeight = FontWeight.Bold
                    )
                ),
                modifier = Modifier.padding(
                    horizontal = Dimensions.Spacing.Medium,
                    vertical = Dimensions.Spacing.ExtraSmall
                )

            )
        }

        if (isSelected) {
            Image(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = "",
                modifier = Modifier.layoutId(CheckId)
                    .size(20.dp)
            )
        }
    }
}

private fun getConstraints() = ConstraintSet {
    val labelBox = createRefFor(LabelBoxId)
    val check = createRefFor(CheckId)

    constrain(check) {
        linkTo(top = parent.top, bottom = parent.bottom, bias = AlignToStart, topMargin = 10.dp)
        end.linkTo(parent.end)
    }

    constrain(labelBox) {
        top.linkTo(parent.top, margin = 10.dp)
        bottom.linkTo(parent.bottom, margin = 10.dp)
        linkTo(start = parent.start, end = parent.end)
    }
}

private const val CheckId = "check"
private const val LabelBoxId = "LabelBox"
