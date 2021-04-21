package com.oscarg798.remembrall.common.repository.domain

import com.oscarg798.remembrall.common.model.Calendar
import com.oscarg798.remembrall.common.model.CalendarSyncInformation

interface CalendarRepository {

    fun getSelectedCalendar(): Calendar

    fun saveSelectedCalendar(calendar: Calendar)

    suspend fun getCalendars(): Collection<Calendar>

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
