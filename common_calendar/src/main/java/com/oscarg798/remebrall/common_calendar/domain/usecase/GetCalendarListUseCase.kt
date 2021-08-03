package com.oscarg798.remebrall.common_calendar.domain.usecase

import com.oscarg798.remebrall.common_calendar.domain.model.Calendar
import com.oscarg798.remebrall.common_calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class GetCalendarListUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {

    suspend fun execute(): Collection<Calendar> {
        return calendarRepository.getCalendars()
    }
}
