package com.oscarg798.remembrall.common.auth

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.user.User
import javax.inject.Inject

@Deprecated("Make this local using session")
class GetSignedInUserUseCase @Inject constructor(private val session: Session) {

    suspend fun execute(): User = (session.getSessionState() as? Session.State.LoggedIn)?.user
        ?: throw IllegalStateException("User not logged in")
}
