package com.oscarg798.remembrall.common_addedit.usecase


import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.CalendarSyncInformation
import com.remembrall.oscarg798.calendar.CalendarRepository
import javax.inject.Inject
import java.time.LocalDateTime

class AddTaskToCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val dueDateFormatter: DateFormatter
) {

    suspend fun execute(
        params: AddTaskToCalendarParams,

    ): CalendarSyncInformation {
        val selectedCalendar = calendarRepository.getSelectedCalendar()

        val startDate = dueDateFormatter.toCalendarTaskDate(params.dueDate)
        val endDate = dueDateFormatter.toCalendarTaskDate(params.dueDate.plusHours(1))
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
