package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.oscarg798.remembrall.ui_common.ui.theming.Dimensions
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
fun getButtonShape() = RoundedCornerShape( RemembrallTheme.dimens.Small)

@Composable
fun getButtonTextStyle() = MaterialTheme.typography.body1.merge(
    TextStyle(
        color = MaterialTheme.colors.onSecondary,
        fontWeight = FontWeight.Bold
    )
)
