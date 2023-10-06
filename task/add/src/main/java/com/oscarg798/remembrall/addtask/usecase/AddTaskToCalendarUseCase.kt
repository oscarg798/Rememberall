package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.CalendarSyncInformation
import com.remembrall.oscarg798.calendar.CalendarRepository
import java.time.LocalDateTime
import javax.inject.Inject

class AddTaskToCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val dateFormatter: DateFormatter
) {

    suspend fun execute(
        params: AddTaskToCalendarParams,
    ): CalendarSyncInformation {
        val selectedCalendar = calendarRepository.getSelectedCalendar()

        val startDate = dateFormatter.toCalendarTaskDate(params.dueDate)
        val endDate = dateFormatter.toCalendarTaskDate(params.dueDate.plusHours(1))
        val calendarEventId = params.taskId.replace("-", "")
        return calendarRepository.addTaskToCalendar(
            calendarId = selectedCalendar.id,
            calendarTask = CalendarRepository.CalendarTask(
                id = calendarEventId,
                summary = params.name,
                startTimeDate = startDate,
                endTimeDate = endDate,
                description = params.description
            ),
            attendees = params.attendees
        )
    }

    data class AddTaskToCalendarParams(
        val taskId: String,
        val name: String,
        val dueDate: LocalDateTime,
        val description: String? = null,
        val attendees: Set<String>? = null
    )
}
