package com.oscarg798.remembrall.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import com.oscarg798.remembrall.ui.dimensions.Dimensions
import com.oscarg798.remembrall.ui.dimensions.LocalAppDimens
import com.oscarg798.remembrall.ui.dimensions.normalDimensions
import com.oscarg798.remembrall.ui.dimensions.smallDimensions
import com.oscarg798.remembrall.ui.typography.getTypography
import com.oscarg798.remembrall.ui.typography.normalFontSizes
import com.oscarg798.remembrall.ui.typography.smallFontSizes
import com.oscarg798.remembrall.uicolor.DarkThemeColors
import com.oscarg798.remembrall.uicolor.LightThemeColors

@Composable
private fun ProvideThemeConfig(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val rememberedDimensions = remember { dimensions }
    CompositionLocalProvider(
        LocalAppDimens provides rememberedDimensions,
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


val LocalFontAwesome = staticCompositionLocalOf<FontFamily> { error("You must provide it first") }
