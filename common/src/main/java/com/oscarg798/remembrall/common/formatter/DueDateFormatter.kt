package com.oscarg798.remembrall.common.formatter

import dagger.Reusable
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Reusable
class DueDateFormatter @Inject constructor() {

    private val locale = Locale.getDefault()
    private val calendarTaskDateFormatter =
        SimpleDateFormat(CalendarDatePattern, locale)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern, locale)
    private val dateFormatter = SimpleDateFormat(DatePattern, locale)
    private val monthDateFormatter = SimpleDateFormat(MonthPattern, locale)
    private val monthNumberFormatter = SimpleDateFormat(MonthNumberPattern, locale);
    private val yearDateFormatter = SimpleDateFormat(YearPattern, locale)
    private val dayDateFormatter = SimpleDateFormat(DayPattern, locale)
    private val dayNameFormatter = SimpleDateFormat(DayNamePattern, locale)
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

    fun toDueLocalDatetime(date: Long): LocalDateTime {
        return Date(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun toCalendarTaskDate(date: Long): String {
        val taskDueDate = toDueLocalDatetime(date)
        val dueDate = taskDueDate.format(dateTimeFormatter)
        dueDateCalendar.time = Date(toDueDateInMillis(dueDate))
        return calendarTaskDateFormatter.format(dueDateCalendar.time)
    }

    fun toCalendarTaskDate(date: LocalDateTime): String {
        val dueDate = date.format(dateTimeFormatter)
        dueDateCalendar.time = Date(toDueDateInMillis(dueDate))
        return calendarTaskDateFormatter.format(dueDateCalendar.time)
    }

    fun getYearFromDueDate(dueDate: Long): String {
        dueDateCalendar.time = Date(dueDate)
        return yearDateFormatter.format(dueDateCalendar.time)
    }

    fun getMonthFromDueDate(dueDate: Long): String {
        dueDateCalendar.time = Date(dueDate)
        return monthDateFormatter.format(dueDateCalendar.time)
    }

    fun getDayFromDueDate(dueDate: Long): String {
        dueDateCalendar.time = Date(dueDate)
        return dayDateFormatter.format(dueDateCalendar.time)
    }

    fun getDayNameFromDueDate(dueDate: Long): String {
        dueDateCalendar.time = Date(dueDate)
        return dayNameFormatter.format(dueDateCalendar.time)
    }

    fun getMonthNumber(monthName: String): String {
        dueDateCalendar.time = monthDateFormatter.parse(monthName)!!
        return monthNumberFormatter.format(dueDateCalendar.time)
    }
}

private const val DayNamePattern = "EEE"
private const val DayPattern = "dd"
private const val YearPattern = "yyyy"
private const val MonthPattern = "MMMM"
private const val MonthNumberPattern = "MM"
private const val CalendarDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val DatePattern = "EEE, MMM dd yyyy HH:mm"
