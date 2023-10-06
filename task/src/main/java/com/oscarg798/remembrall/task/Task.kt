package com.oscarg798.remembrall.task

data class Task(
    val id: String,
    val owner: String,
    val owned: Boolean,
    val title: String,
    val priority: TaskPriority?,
    val calendarSyncInformation: CalendarSyncInformation?,
    val dueDate: Long?,
    val completed: Boolean = false,
    val description: String?,
    val createAt: Long? = null
)
