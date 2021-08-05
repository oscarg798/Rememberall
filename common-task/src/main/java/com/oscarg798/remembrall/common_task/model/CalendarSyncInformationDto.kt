package com.oscarg798.remembrall.common_task.model

import com.oscarg798.remembrall.common.model.CalendarSyncInformation

data class CalendarSyncInformationDto(
    val calendarId: String,
    val calendarEventId: String,
    val synced: Boolean,
    val attendees: Collection<CalendarAttendeeDto>? = null
) {

    constructor(calendarSyncInformation: CalendarSyncInformation) :
        this(
            calendarId = calendarSyncInformation.calendarId,
            synced = calendarSyncInformation.synced,
            calendarEventId = calendarSyncInformation.calendarEventId,
            attendees = calendarSyncInformation.attendees?.let { attendees ->
                attendees.map { CalendarAttendeeDto(it) }
            }
        )

    constructor(taskMap: Map<String, Any?>) : this(
        calendarEventId = taskMap[ColumnNames.CalendarEventId] as? String
            ?: throw IllegalArgumentException("Event Id not found"),
        calendarId = taskMap[ColumnNames.CalendarId] as? String
            ?: throw IllegalArgumentException("Event Id not found"),
        synced = taskMap[ColumnNames.Synced] as? Boolean
            ?: throw IllegalArgumentException("Synced Id not found"),
        attendees = (taskMap[ColumnNames.Attendees] as? List<String>)?.let {
            it.map { attendee -> CalendarAttendeeDto(attendee) }
        }
    )

    fun toCalendarSyncInformation() = CalendarSyncInformation(
        calendarId = calendarId,
        calendarEventId = calendarEventId,
        synced = synced,
        attendees = attendees?.let { attendees ->
            attendees.map { it.toCalendarAttendee() }
        }
    )

    object ColumnNames {
        const val CalendarId = "calendarId"
        const val CalendarEventId = "calendarEventId"
        const val Synced = "synced"
        const val Attendees = "attendees"
    }
}
