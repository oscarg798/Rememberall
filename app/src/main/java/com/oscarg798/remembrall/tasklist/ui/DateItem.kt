package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.RemembrallTextAppearance
import com.oscarg798.remembrall.ui_common.theming.SecondaryTextColor

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
                horizontal = Dimensions.Spacing.Small
            )
        ) {
            Text(
                text = name,
                style = RemembrallTextAppearance.body1.merge(TextStyle(color = SecondaryTextColor))
            )
        }

        Row(
            Modifier
                .padding(
                    start = Dimensions.Spacing.Small,
                    end = Dimensions.Spacing.Small
                )
                .wrapContentSize()
        ) {
            Text(
                text = value,
                style = RemembrallTextAppearance.h2.merge(
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
    MaterialTheme.colors.secondary
} else {
    MaterialTheme.colors.onSurface
}

@Composable
private fun getDividerColor(isToday: Boolean) = if (isToday) {
    MaterialTheme.colors.secondary
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
