package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ProvideThemeConfig(
    dimensions: Dimensions,
    fontSizes: FontSizes,
    content: @Composable () -> Unit
) {
    val rememberedDimensions = remember { dimensions }
    val rememberedFontSizes = remember { fontSizes }
    val rememberedFontAwesome = remember { AwesomeFont }
    CompositionLocalProvider(
        LocalTypography provides rememberedFontSizes,
        LocalAppDimens provides rememberedDimensions,
        LocalFontAwesome provides rememberedFontAwesome
    ) {
        content()
    }
}

@Composable
fun RemembrallTheme(content: @Composable () -> Unit) {
    val fontSizes = if (isSmallScreen()) normalFontSizes else smallFontSizes
    ProvideThemeConfig(
        if (isSmallScreen()) normalDimensions else smallDimensions,
        fontSizes = fontSizes
    ) {
        MaterialTheme(
            colors = if (isSystemInDarkTheme()) DarkColors else LightColors,
            typography = getTypography(fontSizes),
        ) { content() }
    }
}

@Composable
private fun isSmallScreen() = LocalConfiguration.current.screenWidthDp > 360

object RemembrallTheme {

    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}
