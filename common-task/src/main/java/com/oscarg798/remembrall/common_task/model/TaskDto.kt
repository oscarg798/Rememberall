package com.oscarg798.remembrall.common_task.model

import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.model.TaskPriority

data class TaskDto(
    val id: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority,
    val dueDate: Long,
    val completed: Boolean = false,
    val calendarSyncInformation: CalendarSyncInformationDto,

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

    constructor(id: String, taskMap: Map<String, Any>) : this(
        id = id,
        name = taskMap[ColumnNames.Name] as? String ?: throw NullPointerException("No Name found"),
        description = taskMap[ColumnNames.Description] as? String,
        priority = TaskPriority.fromName(
            taskMap[ColumnNames.Priority] as? String
                ?: throw NullPointerException("Priority not found")
        ),
        completed = taskMap[ColumnNames.Completed] as? Boolean
            ?: throw NullPointerException("Completed not found"),
        dueDate = taskMap[ColumnNames.DueDate] as? Long
            ?: throw NullPointerException(
                "Due data not found, " +
                        "but seems it was synced with Calendar"
            ),
        calendarSyncInformation = CalendarSyncInformationDto(taskMap)
    )

    fun toTask() = Task(
        id = id,
        name = name,
        description = description,
        priority = priority,
        completed = completed,
        calendarSyncInformation = calendarSyncInformation.toCalendarSyncInformation(),
        dueDate = dueDate
    )

    fun toMap(): Map<String, Any?> = mutableMapOf(
        ColumnNames.Name to name,
        ColumnNames.Description to description,
        ColumnNames.Priority to priority.javaClass.name,
        ColumnNames.Completed to completed,
        ColumnNames.DueDate to dueDate,
        CalendarSyncInformationDto.ColumnNames.CalendarId to calendarSyncInformation?.calendarId,
        CalendarSyncInformationDto.ColumnNames.CalendarEventId to
                calendarSyncInformation?.calendarEventId,
        CalendarSyncInformationDto.ColumnNames.Synced to calendarSyncInformation?.synced,
        CalendarSyncInformationDto.ColumnNames.Attendees to
                calendarSyncInformation?.attendees?.map { it.email }
    )

    object ColumnNames {
        const val Name = "name"
        const val Description = "description"
        const val Priority = "priority"
        const val Completed = "completed"
        const val DueDate = "dueDate"
    }
}
