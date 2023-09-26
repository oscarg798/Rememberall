package com.oscarg798.remembrall.taskimpl.model

import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskPriority

data class TaskDto(
    val id: String,
    val owner: String,
    val name: String,
    val description: String?,
    val priority: TaskPriority?,
    val dueDate: Long?,
    val completed: Boolean = false,
    val calendarSyncInformation: CalendarSyncInformationDto?,
    val createdAt: Long? = null
) {

    constructor(task: Task) : this(
        id = task.id,
        owner = task.owner,
        name = task.name,
        description = task.description,
        priority = task.priority,
        completed = task.completed,
        calendarSyncInformation = task.calendarSyncInformation?.let {
            CalendarSyncInformationDto(it)
        },
        dueDate = task.dueDate,
        createdAt = task.createAt
    )

    constructor(
        id: String,
        taskMap: Map<String, Any>
    ) : this(
        id = id,
        owner = taskMap[ColumnNames.Owner] as? String
            ?: throw NullPointerException("Tasks must have an user"),
        name = taskMap[ColumnNames.Name] as? String ?: throw NullPointerException("No Name found"),
        description = taskMap[ColumnNames.Description] as? String,
        priority = if (taskMap.keys.contains(ColumnNames.Priority)) {
            TaskPriority.fromName(
                taskMap[ColumnNames.Priority] as? String
                    ?: throw NullPointerException("Priority not found")
            )
        } else {
            null
        },
        completed = taskMap[ColumnNames.Completed] as? Boolean
            ?: throw NullPointerException("Completed not found"),
        dueDate = taskMap[ColumnNames.DueDate] as? Long
            ?: throw NullPointerException(
                "Due data not found, " +
                    "but seems it was synced with Calendar $taskMap"
            ),
        calendarSyncInformation = if (taskMap.hasCalendarInformation()) {
            CalendarSyncInformationDto(taskMap)
        } else {
            null
        },
        createdAt = taskMap[ColumnNames.CreatedAt] as? Long
    )

    fun toTask(owned: Boolean = OwnershipUnknownAtThisPoint) = Task(
        id = id,
        name = name,
        owner = owner,
        description = description,
        priority = priority,
        completed = completed,
        calendarSyncInformation = calendarSyncInformation?.toCalendarSyncInformation(),
        dueDate = dueDate,
        owned = owned,
        createAt = createdAt
    )

    object ColumnNames {
        const val Name = "name"
        const val Description = "description"
        const val Priority = "priority"
        const val Completed = "completed"
        const val DueDate = "dueDate"
        const val Owner = "user"
        const val CreatedAt = "createdAt"
    }

    companion object {
        private fun Map<String, Any>.hasCalendarInformation() =
            CalendarSyncInformationDto.ColumnNames.CalendarId in keys &&
                CalendarSyncInformationDto.ColumnNames.Synced in keys &&
                CalendarSyncInformationDto.ColumnNames.CalendarEventId in keys
    }
}

private const val OwnershipUnknownAtThisPoint = false
