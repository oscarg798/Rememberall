package com.oscarg798.remembrall.common.network.dto

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

    fun toCalendarSyncInformation() = CalendarSyncInformation(
        calendarId = calendarId,
        calendarEventId = calendarEventId,
        synced = synced,
        attendees = attendees?.let { attendees ->
            attendees.map { it.toCalendarAttendee() }
        }
    )
}
