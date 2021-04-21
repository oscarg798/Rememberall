package com.oscarg798.remembrall.common.usecase

import com.oscarg798.remembrall.common.model.Calendar
import com.oscarg798.remembrall.common.repository.domain.CalendarRepository
import javax.inject.Inject

class GetCalendarListUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {

    suspend fun execute(): Collection<Calendar> {
        return calendarRepository.getCalendars()
    }
}
