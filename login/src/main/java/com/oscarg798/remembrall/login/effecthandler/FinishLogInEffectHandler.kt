package com.oscarg798.remembrall.login.effecthandler

import com.oscarg798.remembrall.login.domain.Effect.FinishLogin
import com.oscarg798.remembrall.login.domain.Event
import com.remembrall.oscarg798.calendar.CalendarRepository
import javax.inject.Inject

internal class FinishLogInEffectHandler @Inject constructor(
    private val calendarRepository: CalendarRepository,
) : EffectTransformer<FinishLogin, Event> {

    override suspend fun invoke(effect: FinishLogin): Event {
        return runCatching {
            getOrSelectCalendar()
        }.fold({
            Event.OnSignedIn
        }, {
            if (it !is Exception) throw it
            Event.OnLoginError(it)
        })

    }

    private suspend fun getOrSelectCalendar() = runCatching {
        calendarRepository.getSelectedCalendar()
    }.getOrElse {
        val calendar = calendarRepository.getCalendars().first()
        calendarRepository.saveSelectedCalendar(calendar)
        calendar
    }
}