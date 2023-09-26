package com.oscarg798.remembrall.common.model

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskPriority


data class DisplayableTask(
    val id: String,
    val owned: Boolean,
    val name: String,
    val description: String?,
    val priority: TaskPriority?,
    val completed: Boolean = false,
    val dueDate: String?,
    val attendees: Collection<CalendarAttendee>? = null,

    ) {

    constructor(task: Task, dueDateFormatter: DateFormatter) : this(
        id = task.id,
        owned = task.owned,
        name = task.name,
        description = task.description,
        priority = task.priority ?: TaskPriority.Low,
        completed = task.completed,
        attendees = task.calendarSyncInformation?.attendees,
        dueDate = task.dueDate?.let {
            dueDateFormatter.toDisplayableDate(it)
        }
    )
}