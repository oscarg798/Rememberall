package com.oscarg798.remembrall.taskpriorityextensions

import androidx.compose.ui.graphics.Color
import com.oscarg798.remembrall.uicolor.Amaranth
import com.oscarg798.remembrall.uicolor.BurntSienna
import com.oscarg798.remembrall.uicolor.Froly
import com.oscarg798.remembrall.uicolor.Gorse
import com.oscarg798.remembrall.uicolor.Riptide
import com.oscarg798.remembrall.uicolor.RoseBud
import com.oscarg798.remembrall.uicolor.Shamrock
import com.oscarg798.remembrall.uicolor.SweetCorn


internal sealed class TaskPriorityColorPalette(val color: Color, val background: Color) {
    object Urgent : TaskPriorityColorPalette(color = Amaranth, background = Froly)
    object High : TaskPriorityColorPalette(color = BurntSienna, background = RoseBud)
    object Medium : TaskPriorityColorPalette(color = Gorse, background = SweetCorn)
    object Low : TaskPriorityColorPalette(color = Shamrock, background = Riptide)
}