package com.oscarg798.remembrall

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.theming.Dimensions
import com.oscarg798.remembrall.theming.RemembrallTextAppearance

@Composable
fun DayItem(
    name: String,
    value: String,
    isToday: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimensions.Spacing.Small)
            .width(Width)
            .wrapContentHeight()
    ) {
        Row() {
            Text(
                text = name,
                style = RemembrallTextAppearance.body2.merge(TextStyle(color = Color.Gray))
            )
        }

        Row() {
            Text(text = value,
                style = RemembrallTextAppearance.h3)
        }

        if (isToday) {
            Divider(
                color = Color.Blue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.Spacing.ExtraSmall)
            )
        }
    }
}

private val Width = 30.dp