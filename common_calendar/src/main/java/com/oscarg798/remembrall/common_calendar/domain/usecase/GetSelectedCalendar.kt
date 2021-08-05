package com.oscarg798.remembrall.common_calendar.domain.usecase

import com.oscarg798.remembrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common_calendar.domain.repository.CalendarRepository
import com.oscarg798.remembrall.common_calendar.exception.CalendarNotFoundException
import javax.inject.Inject

class GetSelectedCalendar @Inject constructor(
    private val getCalendars: GetCalendars,
    private val calendarRepository: CalendarRepository
) {

    suspend operator fun invoke(): Calendar {
        return getSelectedCalendar(calendars = getCalendars())
    }

    private fun getSelectedCalendar(calendars: Collection<Calendar>): Calendar {
        return runCatching {
            calendarRepository.getSelectedCalendar()
        }.getOrElse { cause ->
            when (cause) {
                is CalendarNotFoundException -> calendars.firstOrNull {
                    it.isPrimary
                } ?: calendars.first()
                else -> throw cause
            }
        }.also {
            calendarRepository.saveSelectedCalendar(it)
        }
    }
}