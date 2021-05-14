package com.oscarg798.remembrall.common.ui.theming

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.oscarg798.remembrall.R

private val H1FontSize = 28.sp
private val H2FontSize = 24.sp
private val H3FontSize = 16.sp
private val H4FontSize = 14.sp
private val BODY_1_FONT_SIZE = 14.sp
private val BODY_2_FONT_SIZE = 12.sp
private val Subtitle1FontSize = 14.sp
private val Subtitle2FontSize = 12.sp

private val AveriaSans = FontFamily(
    Font(resId = R.font.catamaran_regular, weight = FontWeight.Light),
    Font(resId = R.font.catamaran_medium, weight = FontWeight.Normal),
    Font(resId = R.font.catamaran_bold, weight = FontWeight.Bold)
)
private val H1 =
    TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = H1FontSize,
        fontFamily = AveriaSans
    )
private val H2 =
    TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = H2FontSize,
        fontFamily = AveriaSans
    )
private val H3 =
    TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = H3FontSize,
        fontFamily = AveriaSans
    )
private val H4 =
    TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = H4FontSize,
        fontFamily = AveriaSans
    )
private val Body1 =
    TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = BODY_1_FONT_SIZE,
        fontFamily = AveriaSans
    )
private val Body2 =
    TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = BODY_2_FONT_SIZE,
        fontFamily = AveriaSans
    )

private val Subtitle1 =
    TextStyle(
        fontWeight = FontWeight.Light,
        color = SecondaryTextColor,
        fontSize = Subtitle1FontSize,
        fontFamily = AveriaSans
    )
private val Subtitle2 =
    TextStyle(
        fontWeight = FontWeight.Light,
        color = SecondaryTextColor,
        fontSize = Subtitle2FontSize,
        fontFamily = AveriaSans
    )

val RemembrallTextAppearance = Typography(
    h1 = H1,
    h2 = H2,
    h3 = H3,
    h4 = H4,
    body1 = Body1,
    body2 = Body2,
    subtitle1 = Subtitle1,
    subtitle2 = Subtitle2
)
