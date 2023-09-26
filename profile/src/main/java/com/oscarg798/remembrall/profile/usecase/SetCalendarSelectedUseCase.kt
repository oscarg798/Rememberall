package com.oscarg798.remembrall.profile.usecase

import com.remembrall.oscarg798.calendar.Calendar
import com.remembrall.oscarg798.calendar.CalendarRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SetCalendarSelectedUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {

    suspend fun execute(calendar: Calendar) {
        calendarRepository.saveSelectedCalendar(calendar)
    }
}
