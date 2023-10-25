package com.oscarg798.remembrall.widgetimpl.usecase

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import javax.inject.Inject

internal interface GetSessionState : suspend (Effect.GetSessionState) -> Event.OnSessionStateFound

internal class GetSessionStateImpl @Inject constructor(
    private val session: Session
) : GetSessionState {

    override suspend fun invoke(effect: Effect. GetSessionState): Event.OnSessionStateFound {
        return Event.OnSessionStateFound(session.getSessionState())
    }
}