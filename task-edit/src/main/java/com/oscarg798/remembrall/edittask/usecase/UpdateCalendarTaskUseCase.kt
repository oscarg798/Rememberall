package com.oscarg798.remembrall.edittask.usecase

import com.remembrall.oscarg798.calendar.CalendarRepository

import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.dateformatter.DateFormatter
import javax.inject.Inject

class UpdateCalendarTaskUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val dueDateFormatter: DateFormatter
) {

    suspend fun execute(task: Task) {
//        val calendarSyncInformation = task.calendarSyncInformation
//
//        val dueDateLocalTime = dueDateFormatter.toDueLocalDatetime(task.dueDate!!)
//        val startDate =
//            dueDateFormatter.toCalendarTaskDate(dueDateLocalTime)
//        val endDate = dueDateFormatter.toCalendarTaskDate(dueDateLocalTime.plusHours(1))
//
//        val calendarTask = CalendarRepository.CalendarTask(
//            id = calendarSyncInformation.calendarEventId,
//            summary = task.name,
//            startTimeDate = startDate,
//            endTimeDate = endDate,
//            description = task.description
//        )
//
//        val selectedCalendar = calendarRepository.getSelectedCalendar()
//
//        calendarRepository.updateCalendarEvent(
//            selectedCalendar.id,
//            calendarSyncInformation.calendarEventId,
//            calendarTask,
//            calendarSyncInformation.attendees?.map {
//                it.email
//            }?.toSet()
//        )
    }
}
