package com.oscarg798.remembrall.common.model

data class CalendarSyncInformation(
    val calendarId: String,
    val calendarEventId: String,
    val synced: Boolean,
    val attendees: Collection<CalendarAttendee>? = null
)
