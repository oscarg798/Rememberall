package com.oscarg798.remembrall.tasklist.usecase

import com.oscarg798.remembrall.common.model.DateChooserConfig
import com.oscarg798.remembrall.common.model.Day
import dagger.Reusable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@Reusable
class GetDateChooserConfig @Inject constructor() {

    private val dateFormat = SimpleDateFormat(DAY_NAME_FORMAT, Locale.getDefault())
    private val currentDateFormat = SimpleDateFormat(CURRENT_DATE_FORMAT, Locale.getDefault())

    fun execute(): DateChooserConfig {
        val todayCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        val daysInMonth = todayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        return DateChooserConfig(
            currentDate = currentDateFormat.format(todayCalendar.time),
            days = (1..daysInMonth).map { day ->
                calendar.set(Calendar.DAY_OF_MONTH, day)
                Day(
                    dateFormat.format(calendar.time),
                    calendar.get(Calendar.DAY_OF_MONTH).toString(),
                    calendar.getDay() == todayCalendar.getDay()
                )
            }
        )
    }

    private fun Calendar.getDay() = get(Calendar.DAY_OF_MONTH)
}

private const val CURRENT_DATE_FORMAT = "MMM dd, yyyy"
private const val DAY_NAME_FORMAT = "EEE"
