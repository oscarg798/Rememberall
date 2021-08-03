package com.oscarg798.remembrall.common.model

data class Task(
    val id: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val completed: Boolean = false,
    val calendarSyncInformation: CalendarSyncInformation? = null,
    val dueDate: Long? = null
)
