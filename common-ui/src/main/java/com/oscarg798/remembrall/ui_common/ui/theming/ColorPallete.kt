package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Selago = Color(0xffF0F4FD)
private val Indigo = Color(0xff5C54C7)
private val MineShaft = Color(0xff212121)
private val BurntSienna = Color(0xfff16d56)
private val BlueMarguerite = Color(0xff6b64cc)
private val DownRiver = Color(0xff0C1F4B)
private val SilverSand = Color(0xFFC0BFBF)
private val Shamrock = Color(0xff2cbd99)
private val Gorse = Color(0xFFFFEB3B)
private val Amaranth = Color(0xFFE91E63)
private val RoseBud = Color(0xffF7AEA1)
private val Froly = Color(0xFFf7b7cd)
private val SweetCorn = Color(0xfffaf7d9)
private val Riptide = Color(0xFf87E8D0)

val x = Color(0xffe9e2d9)

val LightColors = lightColors(
    primary = Indigo,
    primaryVariant = BurntSienna,
    secondary = Indigo,
    secondaryVariant = BlueMarguerite,
    background = Selago,
    surface = Color.White,
    error = BurntSienna,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DownRiver,
    onSurface = MineShaft,
    onError = Color.White
)

val DarkColors = darkColors(
    primary = Indigo,
    primaryVariant = BurntSienna,
    secondary = Indigo,
    secondaryVariant = BlueMarguerite,
    background = MineShaft,
    surface = MineShaft,
    error = BurntSienna,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White
)

val SecondaryTextColor = SilverSand

sealed class TaskPriorityColorPalette(val color: Color, val background: Color) {
    object Urgent : TaskPriorityColorPalette(color = Amaranth, background = Froly)
    object High : TaskPriorityColorPalette(color = BurntSienna, background = RoseBud)
    object Medium : TaskPriorityColorPalette(color = Gorse, background = SweetCorn)
    object Low : TaskPriorityColorPalette(color = Shamrock, background = Riptide)
}
