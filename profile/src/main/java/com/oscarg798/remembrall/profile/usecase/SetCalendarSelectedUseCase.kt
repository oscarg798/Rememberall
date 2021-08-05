package com.oscarg798.remembrall.profile.usecase

import com.oscarg798.remembrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common_calendar.domain.repository.CalendarRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SetCalendarSelectedUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {

    fun execute(calendar: Calendar) {
        calendarRepository.saveSelectedCalendar(calendar)
    }
}
