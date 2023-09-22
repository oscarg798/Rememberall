package com.oscarg798.remembrall.login.effecthandler

import com.oscarg798.remembrall.common_calendar.domain.usecase.GetSelectedCalendar
import com.oscarg798.remembrall.login.domain.Effect.FinishLogin
import com.oscarg798.remembrall.login.domain.Event
import javax.inject.Inject

internal class FinishLogInEffectHandler @Inject constructor(
    private val getSelectedCalendar: GetSelectedCalendar
) : EffectTransformer<FinishLogin, Event> {

    override suspend fun invoke(effect: FinishLogin): Event {
        return runCatching {
            getSelectedCalendar()
        }.fold({
            Event.OnSignedIn
        }, {
            if (it !is Exception) throw it
            Event.OnLoginError(it)
        })

    }
}