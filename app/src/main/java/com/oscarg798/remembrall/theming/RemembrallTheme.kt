package com.oscarg798.remembrall.theming

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RemembrallTheme(content: @Composable ()-> Unit){
    MaterialTheme(
        typography = RemembrallTextAppearance,
     //   colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) { content() }
}