package com.oscarg798.remembrall.common_calendar.domain.repository

import com.oscarg798.remembrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common.model.CalendarSyncInformation

interface CalendarRepository {

    fun getSelectedCalendar(): Calendar

    fun saveSelectedCalendar(calendar: Calendar)

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
