package com.oscarg798.remembrall.tasklist.ui.model

import com.oscarg798.remembrall.common.model.TaskPriority

data class DisplayableTask(
    val id: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val completed: Boolean = false,
    val calendarSynced: Boolean = false,
    val dueDate: String? = null
)
