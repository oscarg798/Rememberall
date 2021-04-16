package com.oscarg798.remembrall.theming

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val H1FontSize = 28.sp
private val H2FontSize = 24.sp
private val H3FontSize = 16.sp
private val H4FontSize = 14.sp
private val BODY_1_FONT_SIZE = 14.sp
private val BODY_2_FONT_SIZE = 12.sp

private val H1 =
    TextStyle(fontWeight = FontWeight.Medium, fontSize = H1FontSize)
private val H2 =
    TextStyle(fontWeight = FontWeight.Medium, fontSize = H2FontSize)
private val H3 =
    TextStyle(fontWeight = FontWeight.Medium, fontSize = H3FontSize)
private val H4 =
    TextStyle(fontWeight = FontWeight.Medium, fontSize = H4FontSize)
private val Body1 =
    TextStyle(fontWeight = FontWeight.Normal, fontSize = BODY_1_FONT_SIZE)
private val Body2 =
    TextStyle(fontWeight = FontWeight.Normal, fontSize = BODY_2_FONT_SIZE)

val RemembrallTextAppearance = Typography(h1 = H1, h2 = H2, h3 = H3, h4 = H4, body1 = Body1, body2 = Body2)