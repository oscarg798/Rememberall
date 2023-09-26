package com.remembrall.oscarg798.calendar

import com.oscarg798.remembrall.task.CalendarSyncInformation

interface CalendarRepository {

    /**
     * We will attempt to get a previously saved calendar or select a default one
     * We expect any user to have at least 1 default calendar
     */
    suspend fun getSelectedCalendar(): Calendar

    suspend fun saveSelectedCalendar(calendar: Calendar)

    suspend fun getCalendars(): Collection<Calendar>

    suspend fun updateCalendarEvent(
        calendarId: String,
        calendarEventId: String,
        calendarTask: CalendarTask,
        attendees: Set<String>?
    ): CalendarSyncInformation

    suspend fun addTaskToCalendar(
        calendarId: String,
        calendarTask: CalendarTask,
        attendees: Set<String>?
    ): CalendarSyncInformation

    class CalendarTask(
        val id: String,
        val summary: String,
        val startTimeDate: String,
        val endTimeDate: String,
        val description: String? = null
    )
}
