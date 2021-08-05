package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.compose.material.Typography
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
        get() = 24.sp
    override val H2: TextUnit
        get() = 20.sp
    override val H3: TextUnit
        get() = 14.sp
    override val H4: TextUnit
        get() = 12.sp
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
        get() = 28.sp
    override val H2: TextUnit
        get() = 24.sp
    override val H3: TextUnit
        get() = 16.sp
    override val H4: TextUnit
        get() = 14.sp
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
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H1,
        fontFamily = AveriaSans
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H2,
        fontFamily = AveriaSans
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H3,
        fontFamily = AveriaSans
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSizes.H4,
        fontFamily = AveriaSans
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = fontSizes.Body1,
        fontFamily = AveriaSans
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = fontSizes.Body2,
        fontFamily = AveriaSans
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Light,
        color = SecondaryTextColor,
        fontSize = fontSizes.Subtitle1FontSize,
        fontFamily = AveriaSans
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Light,
        color = SecondaryTextColor,
        fontSize = fontSizes.Subtitle2FontSize,
        fontFamily = AveriaSans
    )
)

internal val LocalTypography = staticCompositionLocalOf<FontSizes> {
    error("You must provide a typography")
}

val LocalFontAwesome = staticCompositionLocalOf<FontFamily> {
    error("You must provide it first")
}