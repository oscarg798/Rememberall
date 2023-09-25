package com.oscarg798.remembrall.edittask.model

import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskPriority

data class EditableTask(
    val task: Task,
    val displayableTask: DisplayableTask,
    val availablePriorities: List<TaskPriority>,
    val attendees: Collection<CalendarAttendee> = emptySet()
)
