package com.oscarg798.remembrall.common.model

import com.oscarg798.remembrall.common.formatter.DueDateFormatter

data class DisplayableTask(
    val id: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val completed: Boolean = false,
    val calendarSynced: Boolean = false,
    val attendees: Collection<CalendarAttendee>? = null,
    val dueDate: String? = null
) {

    constructor(task: Task, dueDateFormatter: DueDateFormatter) : this(
        id = task.id,
        name = task.name,
        description = task.description,
        priority = task.priority,
        completed = task.completed,
        calendarSynced = task.calendarSyncInformation != null,
        attendees = task.calendarSyncInformation?.attendees,
        dueDate =
        if (task.dueDate != null) dueDateFormatter.toDisplayableDate(task.dueDate) else null
    )
}
