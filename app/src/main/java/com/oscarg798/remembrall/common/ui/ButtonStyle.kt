package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.oscarg798.remembrall.common.ui.theming.Dimensions

fun getButtonShape() = RoundedCornerShape(Dimensions.CornerRadius.Small)

@Composable
fun getButtonTextStyle() = MaterialTheme.typography.body1.merge(
    TextStyle(
        color = MaterialTheme.colors.onSecondary,
        fontWeight = FontWeight.Bold
    )
)
