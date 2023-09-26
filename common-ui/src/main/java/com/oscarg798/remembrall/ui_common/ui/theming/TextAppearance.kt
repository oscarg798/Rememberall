package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.oscarg798.remembrall.ui_common.R

interface FontSizes {

    val H1: TextUnit
    val H2: TextUnit
    val H3: TextUnit
    val H4: TextUnit
    val Body1: TextUnit
    val Body2: TextUnit
    val Subtitle1FontSize: TextUnit
    val Subtitle2FontSize: TextUnit
}

val smallFontSizes = object : FontSizes {
    override val H1: TextUnit
        get() = 28.sp
    override val H2: TextUnit
        get() = 24.sp
    override val H3: TextUnit
        get() = 20.sp
    override val H4: TextUnit
        get() = 14.sp
    override val Body1: TextUnit
        get() = 12.sp
    override val Body2: TextUnit
        get() = 10.sp
    override val Subtitle1FontSize: TextUnit
        get() = 12.sp
    override val Subtitle2FontSize: TextUnit
        get() = 10.sp
}

val normalFontSizes = object : FontSizes {
    override val H1: TextUnit
        get() = 32.sp
    override val H2: TextUnit
        get() = 28.sp
    override val H3: TextUnit
        get() = 24.sp
    override val H4: TextUnit
        get() = 18.sp
    override val Body1: TextUnit
        get() = 14.sp
    override val Body2: TextUnit
        get() = 12.sp
    override val Subtitle1FontSize: TextUnit
        get() = 14.sp
    override val Subtitle2FontSize: TextUnit
        get() = 12.sp
}

private val AveriaSans = FontFamily(
    Font(resId = R.font.catamaran_regular, weight = FontWeight.Light),
    Font(resId = R.font.catamaran_medium, weight = FontWeight.Normal),
    Font(resId = R.font.catamaran_bold, weight = FontWeight.Bold)
)
internal val AwesomeFont = FontFamily(
    Font(resId = R.font.font_awesome_regular, weight = FontWeight.Normal),
    Font(resId = R.font.font_awesome_brand, weight = FontWeight.Medium),
    Font(resId = R.font.font_awesome_solid, weight = FontWeight.Bold)
)

internal fun getTypography(fontSizes: FontSizes): Typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H1,
        fontFamily = AveriaSans
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H1,
        fontFamily = AveriaSans
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H2,
        fontFamily = AveriaSans
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H2,
        fontFamily = AveriaSans
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H2,
        fontFamily = AveriaSans
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H3,
        fontFamily = AveriaSans
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H3,
        fontFamily = AveriaSans
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H3,
        fontFamily = AveriaSans
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H4,
        fontFamily = AveriaSans
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H4,
        fontFamily = AveriaSans
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.Body1,
        fontFamily = AveriaSans
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.Body1,
        fontFamily = AveriaSans
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.Body2,
        fontFamily = AveriaSans
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.Body2,
        fontFamily = AveriaSans
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.Body2,
        fontFamily = AveriaSans
    )

)

val LocalFontAwesome = staticCompositionLocalOf<FontFamily> { error("You must provide it first") }
