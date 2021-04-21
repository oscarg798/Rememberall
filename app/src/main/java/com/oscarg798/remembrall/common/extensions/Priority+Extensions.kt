package com.oscarg798.remembrall.common.extensions

import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.ui.theming.TaskPriorityColorPallete

fun TaskPriority.getLabel() = when (this) {
    TaskPriority.High -> R.string.priority_high_label
    TaskPriority.Low -> R.string.priority_low_label
    TaskPriority.Medium -> R.string.priority_medium_label
    TaskPriority.Urgent -> R.string.priority_urgent_label
}

fun TaskPriority.getColor() = when (this) {
    TaskPriority.High -> TaskPriorityColorPallete.High.color
    TaskPriority.Low -> TaskPriorityColorPallete.Low.color
    TaskPriority.Medium -> TaskPriorityColorPallete.Medium.color
    TaskPriority.Urgent -> TaskPriorityColorPallete.Urgent.color
}

fun TaskPriority.getBackgroundColor() = when (this) {
    TaskPriority.High -> TaskPriorityColorPallete.High.background
    TaskPriority.Low -> TaskPriorityColorPallete.Low.background
    TaskPriority.Medium -> TaskPriorityColorPallete.Medium.background
    TaskPriority.Urgent -> TaskPriorityColorPallete.Urgent.background
}
