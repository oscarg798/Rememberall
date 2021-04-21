package com.oscarg798.remembrall.common.network.dto

import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.model.TaskPriority

data class TaskDto(
    val id: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val completed: Boolean = false,
    val calendarSyncInformation: CalendarSyncInformationDto? = null,
    val dueDate: Long? = null
) {

    constructor(task: Task) : this(
        id = task.id,
        name = task.name,
        description = task.description,
        priority = task.priority,
        completed = task.completed,
        calendarSyncInformation = task.calendarSyncInformation?.let {
            CalendarSyncInformationDto(it)
        },
        dueDate = task.dueDate
    )

    constructor(task: Task, taskCalendarSyncInformation: CalendarSyncInformation) : this(
        id = task.id,
        name = task.name,
        description = task.description,
        priority = task.priority,
        completed = task.completed,
        calendarSyncInformation = CalendarSyncInformationDto(taskCalendarSyncInformation),
        dueDate = task.dueDate
    )

    fun toTask() = Task(
        id = id,
        name = name,
        description = description,
        priority = priority,
        completed = completed,
        calendarSyncInformation = calendarSyncInformation?.toCalendarSyncInformation(),
        dueDate = dueDate
    )
}
