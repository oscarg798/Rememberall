package com.oscarg798.remembrall.edittask.model

import com.oscarg798.remembrall.common.model.CalendarAttendee
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.model.TaskPriority

data class EditableTask(
    val task: Task,
    val displayableTask: DisplayableTask,
    val availablePriorities: List<TaskPriority>,
    val attendees: Collection<CalendarAttendee> = emptySet()
)
