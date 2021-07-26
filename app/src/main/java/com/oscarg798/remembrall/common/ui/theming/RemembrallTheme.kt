package com.oscarg798.remembrall.common.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RemembrallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = RemembrallTextAppearance,
    ) { content() }
}
