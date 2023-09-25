package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ProvideThemeConfig(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val rememberedDimensions = remember { dimensions }
    val rememberedFontAwesome = remember { AwesomeFont }
    CompositionLocalProvider(
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
    ) {
        MaterialTheme(
            colorScheme = if (isSystemInDarkTheme()) DarkThemeColors else LightThemeColors,
            typography = getTypography(fontSizes)
        ) { content() }
    }
}

@Composable
private fun isSmallScreen() = LocalConfiguration.current.screenWidthDp > 360

val MaterialTheme.dimensions
    @Composable
    get() = LocalAppDimens.current

val androidx.compose.material.MaterialTheme.dimensions
    @Composable
    get() = LocalAppDimens.current

val MaterialTheme.typo
    @Composable
    get() = androidx.compose.material.MaterialTheme.typography

val androidx.compose.material.MaterialTheme.colorScheme
    @Composable
    get() = MaterialTheme.colorScheme

object RemembrallTheme {

    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}
