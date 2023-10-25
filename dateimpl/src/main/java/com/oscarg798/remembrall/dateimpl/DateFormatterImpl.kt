package com.oscarg798.remembrall.dateimpl

import com.oscarg798.remembrall.dateformatter.DateFormatter
import dagger.Reusable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@Reusable
class DateFormatterImpl @Inject constructor() : DateFormatter {

    private val locale = Locale.getDefault()
    private val calendarTaskDateFormatter =
        SimpleDateFormat(CalendarDatePattern, locale)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimePattern, locale)
    private val dateFormatter = SimpleDateFormat(DateTimePattern, locale)
    private val shortDateFormatter = SimpleDateFormat(DatePattern, locale)
    private val monthDateFormatter = SimpleDateFormat(MonthPattern, locale)
    private val monthNumberFormatter = SimpleDateFormat(MonthNumberPattern, locale)
    private val yearDateFormatter = SimpleDateFormat(YearPattern, locale)
    private val dayDateFormatter = SimpleDateFormat(DayPattern, locale)
    private val dayNameFormatter = SimpleDateFormat(DayNamePattern, locale)

    override fun toDisplayableDate(date: Long, short: Boolean): String {
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(date)
        return if (short) {
            shortDateFormatter.format(dateCalendar.time)
        } else {
            dateFormatter.format(dateCalendar.time)
        }
    }

    override fun toDisplayableDate(date: LocalDateTime, short: Boolean): String {
        return if (short) {
            val dateCalendar = dateCalendar()
            dateCalendar.time = Date(toMillis(date))
            shortDateFormatter.format(dateCalendar.time)
        } else {
            date.format(dateTimeFormatter)
        }
    }

    override fun toMillis(date: String): Long {
        val date = dateFormatter.parse(date)
        val dateCalendar = dateCalendar()
        dateCalendar.time = date ?: error("Unable to parse $date")
        return dateCalendar.timeInMillis
    }

    override fun toMillis(date: LocalDateTime): Long {
        return toMillis(toDisplayableDate(date))
    }

    override fun toLocalDatetime(date: Long): LocalDateTime {
        return Date(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    override fun toCalendarTaskDate(date: Long): String {
        val taskDueDate = toLocalDatetime(date)
        val dueDate = taskDueDate.format(dateTimeFormatter)
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(toMillis(dueDate))
        return calendarTaskDateFormatter.format(dateCalendar.time)
    }

    override fun toCalendarTaskDate(date: LocalDateTime): String {
        val dueDate = date.format(dateTimeFormatter)
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(toMillis(dueDate))
        return calendarTaskDateFormatter.format(dateCalendar.time)
    }

    override fun getYearFromDate(dateInMillis: Long): String {
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(dateInMillis)
        return yearDateFormatter.format(dateCalendar.time)
    }

    override fun getMonthFromDate(date: Long): String {
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(date)
        return monthDateFormatter.format(dateCalendar.time)
    }

    override fun getDayFromDate(dateInMillis: Long): String {
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(dateInMillis)
        return dayDateFormatter.format(dateCalendar.time)
    }

    override fun getDayNameFromDate(date: Long): String {
        val dateCalendar = dateCalendar()
        dateCalendar.time = Date(date)
        return dayNameFormatter.format(dateCalendar.time)
    }

    override fun getMonthNumber(monthName: String): String {
        val dateCalendar = dateCalendar()
        dateCalendar.time = monthDateFormatter.parse(monthName)!!
        return monthNumberFormatter.format(dateCalendar.time)
    }

    private fun dateCalendar() = Calendar.getInstance()
}

private const val DayNamePattern = "EEE"
private const val DayPattern = "dd"
private const val YearPattern = "yyyy"
private const val MonthPattern = "MMMM"
private const val MonthNumberPattern = "MM"
private const val CalendarDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val DateTimePattern = "EEE, MMM dd yyyy, HH:mm"
private const val DatePattern = "MMM dd yyyy"
