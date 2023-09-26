package com.oscarg798.remembrall.task

data class CalendarSyncInformation(
    val calendarId: String,
    val calendarEventId: String,
    val synced: Boolean,
    val attendees: Collection<CalendarAttendee>? = null
)
