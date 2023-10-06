package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.DueDate
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.CalendarSyncInformation
import com.remembrall.oscarg798.calendar.CalendarRepository
import javax.inject.Inject

internal interface UpdateCalendarInformation :
    suspend (
        String,
        String?,
        CalendarSyncInformation,
        DueDate,
        Set<String>
    ) -> CalendarSyncInformation

internal class UpdateCalendarInformationImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val calendarRepository: CalendarRepository,
) : UpdateCalendarInformation {

    override suspend fun invoke(
        title: String,
        description: String?,
        calendarSyncInformation: CalendarSyncInformation,
        dueDate: DueDate,
        attendees: Set<String>
    ): CalendarSyncInformation {
        val startDate = dateFormatter.toCalendarTaskDate(dueDate.date)
        val endDate = dateFormatter.toCalendarTaskDate(dueDate.date.plusHours(1))

        val calendarTask = CalendarRepository.CalendarTask(
            id = calendarSyncInformation.calendarEventId,
            summary = title,
            startTimeDate = startDate,
            endTimeDate = endDate,
            description = description
        )

        return calendarRepository.updateCalendarEvent(
            calendarId = calendarSyncInformation.calendarId,
            calendarEventId = calendarSyncInformation.calendarEventId,
            calendarTask = calendarTask,
            attendees = attendees
        )
    }
}