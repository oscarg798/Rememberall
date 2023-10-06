package com.oscarg798.remembrall.list.model

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskPriority

data class DisplayableTask(
    val id: String,
    val owned: Boolean,
    val title: String,
    val description: String? = null,
    val priority: TaskPriority? = null,
    val completed: Boolean = false,
    val dueDate: String? = null,
    val attendees: Collection<CalendarAttendee>? = null,

    ) {

    @Deprecated("DateFormatter should not be added as dependency here")
    constructor(task: Task, dueDateFormatter: DateFormatter) : this(
        id = task.id,
        owned = task.owned,
        title = task.title,
        description = task.description,
        priority = task.priority ?: TaskPriority.Low,
        completed = task.completed,
        attendees = task.calendarSyncInformation?.attendees,
        dueDate = task.dueDate?.let {
            dueDateFormatter.toDisplayableDate(it)
        }
    )
}
