package com.oscarg798.remembrall.detail.domain

import com.oscarg798.rememberall.task.descriptionformatter.Description
import com.oscarg798.remembrall.task.TaskPriority

data class DisplayableTask(
    val id: String,
    val owned: Boolean,
    val title: String,
    val attendees: Set<String>,
    val description: Description? = null,
    val priority: TaskPriority? = null,
    val completed: Boolean = false,
    val dueDate: String? = null,
)