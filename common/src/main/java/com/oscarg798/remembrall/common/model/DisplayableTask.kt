package com.oscarg798.remembrall.common.model

import com.oscarg798.remembrall.dateformatter.DueDateFormatter


data class DisplayableTask(
    val id: String,
    val owned: Boolean,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val completed: Boolean = false,
    val dueDate: String,
    val attendees: Collection<CalendarAttendee>? = null,

    ) {

    constructor(task: Task, dueDateFormatter: DueDateFormatter) : this(
        id = task.id,
        owned = task.owned,
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
