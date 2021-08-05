package com.oscarg798.remembrall.common_calendar.domain.usecase

import com.oscarg798.remembrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common_calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class GetCalendars @Inject constructor(
    private val calendarRepository: CalendarRepository
) {

    suspend operator fun invoke(): Collection<Calendar> {
        return calendarRepository.getCalendars()
    }
}
