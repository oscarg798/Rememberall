package com.oscarg798.remembrall.common.model

import com.oscarg798.remembrall.common.formatter.DueDateFormatter

data class DisplayableTask(
    val id: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val completed: Boolean = false,
    val dueDate: String,
    val attendees: Collection<CalendarAttendee>? = null,

    ) {

    constructor(task: Task, dueDateFormatter: DueDateFormatter) : this(
        id = task.id,
        name = task.name,
        description = task.description,
        priority = task.priority,
        completed = task.completed,
        attendees = task.calendarSyncInformation.attendees,
        dueDate = dueDateFormatter.toDisplayableDate(
            task.dueDate
        )
    )
}
