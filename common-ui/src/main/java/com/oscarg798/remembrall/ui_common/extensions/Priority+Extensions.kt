package com.oscarg798.remembrall.ui_common.extensions

import com.oscarg798.remembrall.task.TaskPriority
import com.oscarg798.remembrall.ui_common.R
import com.oscarg798.remembrall.ui_common.ui.theming.TaskPriorityColorPalette

// TODO move this to its own module
fun TaskPriority.getLabel() = when (this) {
    TaskPriority.High -> R.string.priority_high_label
    TaskPriority.Low -> R.string.priority_low_label
    TaskPriority.Medium -> R.string.priority_medium_label
    TaskPriority.Urgent -> R.string.priority_urgent_label
}

fun TaskPriority.getColor() = when (this) {
    TaskPriority.High -> TaskPriorityColorPalette.High.color
    TaskPriority.Low -> TaskPriorityColorPalette.Low.color
    TaskPriority.Medium -> TaskPriorityColorPalette.Medium.color
    TaskPriority.Urgent -> TaskPriorityColorPalette.Urgent.color
}

fun TaskPriority.getBackgroundColor() = when (this) {
    TaskPriority.High -> TaskPriorityColorPalette.High.background
    TaskPriority.Low -> TaskPriorityColorPalette.Low.background
    TaskPriority.Medium -> TaskPriorityColorPalette.Medium.background
    TaskPriority.Urgent -> TaskPriorityColorPalette.Urgent.background
}
