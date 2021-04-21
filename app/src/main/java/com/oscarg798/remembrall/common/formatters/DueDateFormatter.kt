package com.oscarg798.remembrall.common.formatters

import dagger.Reusable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@Reusable
class DueDateFormatter @Inject constructor() {

    private val calendarTaskDateFormatter =
        SimpleDateFormat(CalendarDatePattern, Locale.getDefault())
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern, Locale.getDefault())
    private val dateFormatter = SimpleDateFormat(DatePattern, Locale.getDefault())
    private val dueDateCalendar = Calendar.getInstance()

    fun toDisplayableDate(dueDate: Long): String {
        dueDateCalendar.time = Date(dueDate)

        return dateFormatter.format(dueDateCalendar.time)
    }

    fun toDisplayableDate(date: LocalDateTime): String {
        return date.format(dateTimeFormatter)
    }

    fun toDueDateInMillis(dueDate: String): Long {
        val date = dateFormatter.parse(dueDate)
        dueDateCalendar.time = date ?: error("Unable to parse $dueDate")
        return dueDateCalendar.timeInMillis
    }

    fun toDueDateInMillis(date: LocalDateTime): Long {
        return toDueDateInMillis(toDisplayableDate(date))
    }

    fun toCalendarTaskDate(date: LocalDateTime): String {
        val dueDate = date.format(dateTimeFormatter)
        dueDateCalendar.time = Date(toDueDateInMillis(dueDate))
        return calendarTaskDateFormatter.format(dueDateCalendar.time)
    }
}

private const val CalendarDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val DatePattern = "EEE, MMM dd HH:mm"
