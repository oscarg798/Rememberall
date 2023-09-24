package com.oscarg798.remembrall.dateimpl

import com.oscarg798.remembrall.dateformatter.DueDateFormatter
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
class DueDateFormatterImpl @Inject constructor() : DueDateFormatter {

    private val locale = Locale.getDefault()
    private val calendarTaskDateFormatter =
        SimpleDateFormat(CalendarDatePattern, locale)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimePattern, locale)
    private val dateFormatter = SimpleDateFormat(DateTimePattern, locale)
    private val shortDateFormatter = SimpleDateFormat(DatePattern, locale)
    private val monthDateFormatter = SimpleDateFormat(MonthPattern, locale)
    private val monthNumberFormatter = SimpleDateFormat(MonthNumberPattern, locale);
    private val yearDateFormatter = SimpleDateFormat(YearPattern, locale)
    private val dayDateFormatter = SimpleDateFormat(DayPattern, locale)
    private val dayNameFormatter = SimpleDateFormat(DayNamePattern, locale)

    override fun toDisplayableDate(dueDate: Long, short: Boolean): String {
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(dueDate)
        return if (short) {
            shortDateFormatter.format(dueDateCalendar.time)
        } else {
            dateFormatter.format(dueDateCalendar.time)
        }
    }

    override fun toDisplayableDate(date: LocalDateTime, short: Boolean): String {
        return if (short) {
            val dueDateCalendar = dueDateCalendar()
            dueDateCalendar.time = Date(toDueDateInMillis(date))
            shortDateFormatter.format(dueDateCalendar.time)
        } else {
            date.format(dateTimeFormatter)
        }
    }

    override fun toDueDateInMillis(dueDate: String): Long {
        val date = dateFormatter.parse(dueDate)
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = date ?: error("Unable to parse $dueDate")
        return dueDateCalendar.timeInMillis
    }

    override fun toDueDateInMillis(date: LocalDateTime): Long {
        return toDueDateInMillis(toDisplayableDate(date))
    }

    override fun toDueLocalDatetime(date: Long): LocalDateTime {
        return Date(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    override fun toCalendarTaskDate(date: Long): String {
        val taskDueDate = toDueLocalDatetime(date)
        val dueDate = taskDueDate.format(dateTimeFormatter)
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(toDueDateInMillis(dueDate))
        return calendarTaskDateFormatter.format(dueDateCalendar.time)
    }

    override fun toCalendarTaskDate(date: LocalDateTime): String {
        val dueDate = date.format(dateTimeFormatter)
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(toDueDateInMillis(dueDate))
        return calendarTaskDateFormatter.format(dueDateCalendar.time)
    }

    override fun getYearFromDueDate(dueDate: Long): String {
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(dueDate)
        return yearDateFormatter.format(dueDateCalendar.time)
    }

    override fun getMonthFromDueDate(dueDate: Long): String {
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(dueDate)
        return monthDateFormatter.format(dueDateCalendar.time)
    }

    override fun getDayFromDueDate(dueDate: Long): String {
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(dueDate)
        return dayDateFormatter.format(dueDateCalendar.time)
    }

    override fun getDayNameFromDueDate(dueDate: Long): String {
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = Date(dueDate)
        return dayNameFormatter.format(dueDateCalendar.time)
    }

    override fun getMonthNumber(monthName: String): String {
        val dueDateCalendar = dueDateCalendar()
        dueDateCalendar.time = monthDateFormatter.parse(monthName)!!
        return monthNumberFormatter.format(dueDateCalendar.time)
    }

    private fun dueDateCalendar() = Calendar.getInstance()
}

private const val DayNamePattern = "EEE"
private const val DayPattern = "dd"
private const val YearPattern = "yyyy"
private const val MonthPattern = "MMMM"
private const val MonthNumberPattern = "MM"
private const val CalendarDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val DateTimePattern = "EEE, MMM dd yyyy, HH:mm"
private const val DatePattern = "EEE, MMM dd yyyy"
