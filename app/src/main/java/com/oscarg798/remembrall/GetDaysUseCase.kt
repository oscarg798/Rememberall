package com.oscarg798.remembrall

import com.oscarg798.remembrall.model.Day
import java.text.SimpleDateFormat
import java.util.*

class GetDaysUseCase() {

    private val dateFormat = SimpleDateFormat(DAY_NAME_FORMAT, Locale.getDefault())

    fun execute(): List<Day> {
        val todayCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        val currentMonth = todayCalendar.get(Calendar.MONTH)
        val daysInMonth = todayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentYear = todayCalendar.get(Calendar.YEAR)

        return (1..daysInMonth).map { day->
            Day(dateFormat.format(calendar.time), calendar.get(Calendar.DAY_OF_MONTH).toString(),
            day == todayCalendar.get(Calendar.DAY_OF_MONTH))
        }
    }
}

private const val DAY_NAME_FORMAT = "EEE"