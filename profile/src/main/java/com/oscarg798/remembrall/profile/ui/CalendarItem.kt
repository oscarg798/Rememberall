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
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.extensions.SingleLine
import com.oscarg798.remembrall.ui.icons.R as IconsR
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.uicolor.SecondaryTextColor
import com.remembrall.oscarg798.calendar.Calendar

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
                style = MaterialTheme.typography.bodyMedium
                    .merge(TextStyle(color = MaterialTheme.colorScheme.onSurface)),
                maxLines = SingleLine,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(RemembrallTheme.dimens.Medium).weight(
                    if (isSelected) {
                        0.8f
                    } else {
                        1f
                    }
                )
            )

            if (isSelected) {
                Image(
                    painter = painterResource(IconsR.drawable.ic_check),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(RemembrallTheme.dimens.Medium)
                        .weight(0.2f)
                        .size(SelectedCalendarIndicatorHeight)
                )
            }
        }

        if (divider) {
            Divider(
                modifier = Modifier
                    .padding(
                        horizontal = RemembrallTheme.dimens.Medium,
                        vertical = RemembrallTheme.dimens.ExtraSmall
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
