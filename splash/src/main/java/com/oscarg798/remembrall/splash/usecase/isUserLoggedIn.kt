package com.oscarg798.remembrall.splash.usecase

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.splash.domain.Event
import javax.inject.Inject

internal interface IsUserLoggedIn : suspend () -> Event

internal class IsUserLoggedInImpl @Inject constructor(
    private val session: Session
) : IsUserLoggedIn {

    override suspend fun invoke(): Event = Event.OnSessionStateObtained(session.getLoggedInState())

}
