package com.oscarg798.remembrall.common.usecase

import com.oscarg798.remebrall.common_calendar.domain.repository.CalendarRepository
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class AddTaskToCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val taskRepository: TaskRepository,
    private val dueDateFormatter: DueDateFormatter
) {

    suspend fun execute(
        task: Task,
        dueDate: LocalDateTime,
        attendees: Set<String>?
    ) {

        val selectedCalendar = calendarRepository.getSelectedCalendar()

        val startDate = dueDateFormatter.toCalendarTaskDate(dueDate)
        val endDate = dueDateFormatter.toCalendarTaskDate(dueDate.plusHours(1))
        val calendarEventId = task.id.replace("-", "")
        val calendarSyncInformation = calendarRepository.addTaskToCalendar(
            calendarId = selectedCalendar.id,
            calendarTask = CalendarRepository.CalendarTask(
                id = calendarEventId,
                summary = task.name,
                startTimeDate = startDate,
                endTimeDate = endDate,
                description = task.description
            ),
            attendees = attendees
        )

        taskRepository.updateWithCalendarInformation(
            tasksCalendarSyncInformation = calendarSyncInformation,
            task = task
        )
    }
}
