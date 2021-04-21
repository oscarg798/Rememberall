package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.SingleLine
import com.oscarg798.remembrall.common.model.Calendar
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.common.ui.theming.SecondaryTextColor

@Composable
fun CalendarItem(
    calendar: Calendar,
    isSelected: Boolean,
    divider: Boolean,
    onCalendarSelection: (Calendar) -> Unit,
) {

    Column(modifier = Modifier.clickable { onCalendarSelection(calendar) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = calendar.name,
                style = MaterialTheme.typography.body1
                    .merge(TextStyle(color = MaterialTheme.colors.onSurface)),
                maxLines = SingleLine,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(Dimensions.Spacing.Medium).weight(
                    if (isSelected) {
                        0.8f
                    } else {
                        1f
                    }
                )
            )

            if (isSelected) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(Dimensions.Spacing.Medium)
                        .weight(0.2f)
                        .size(SelectedCalendarIndicatorHeight)
                )
            }
        }

        if (divider) {
            Divider(
                modifier = Modifier
                    .padding(
                        horizontal = Dimensions.Spacing.Medium,
                        vertical = Dimensions.Spacing.ExtraSmall
                    )
                    .fillMaxWidth()
                    .width(DividerHeight),
                color = SecondaryTextColor
            )
        }
    }
}

private val SelectedCalendarIndicatorHeight = 20.dp
private val DividerHeight = 1.dp
