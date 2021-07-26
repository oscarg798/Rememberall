package com.oscarg798.remembrall.addtask.ui

import com.oscarg798.remembrall.common.model.TaskPriority

data class AddtaskScreenConfiguration(
    val availablePriorities: List<TaskPriority>,
    val selectedPriority: TaskPriority
)
