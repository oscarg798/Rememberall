package com.oscarg798.remembrall.common.model

data class EditableTask(
    val task: Task,
    val displayableTask: DisplayableTask,
    val availablePriorities: List<TaskPriority>,
    val attendees: Collection<CalendarAttendee> = emptySet()
)
