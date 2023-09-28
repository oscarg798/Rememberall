package com.oscarg798.remembrall.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.uicolor.SecondaryTextColor

@Composable
fun DayItem(
    name: String = "Mon",
    value: String = "11",
    isToday: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(Width)
            .wrapContentHeight()
    ) {
        Row(
            Modifier.padding(
                horizontal = RemembrallTheme.dimens.Small
            )
        ) {
            Text(
                text = name,
                style =
                MaterialTheme.typography.bodyMedium.merge(TextStyle(color = SecondaryTextColor))
            )
        }

        Row(
            Modifier
                .padding(
                    start = RemembrallTheme.dimens.Small,
                    end = RemembrallTheme.dimens.Small
                )
                .wrapContentSize()
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall.merge(
                    TextStyle(
                        color = getDayColor(isToday)
                    )
                )
            )
        }

        Divider(
            color = getDividerColor(isToday),
            modifier = Modifier
                .fillMaxWidth()
                .height(getDividerHeight(isToday))
        )
    }
}

@Composable
private fun getDayColor(isToday: Boolean) = if (isToday) {
    MaterialTheme.colorScheme.secondary
} else {
    MaterialTheme.colorScheme.onSurface
}

@Composable
private fun getDividerColor(isToday: Boolean) = if (isToday) {
    MaterialTheme.colorScheme.secondary
} else {
    SecondaryTextColor
}

private fun getDividerHeight(isToday: Boolean) = if (isToday) {
    TodayDividerHeight
} else {
    DefaultDividerHeight
}

private val DefaultDividerHeight = 2.dp
private val TodayDividerHeight = 2.dp
private val Width = 55.dp
