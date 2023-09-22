package com.oscarg798.remembrall.splash.usecase

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.common.auth.AuthRepository
import javax.inject.Inject

internal interface IsUserLoggedIn : suspend () -> Boolean

internal class IsUserLoggedInImpl @Inject constructor(
    private val session: Session
) : IsUserLoggedIn {

    override suspend fun invoke(): Boolean = session.getLoggedInState() is Session.State.LoggedIn
}
