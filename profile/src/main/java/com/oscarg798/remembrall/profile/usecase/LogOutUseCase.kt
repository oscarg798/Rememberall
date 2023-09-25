package com.oscarg798.remembrall.profile.usecase

import com.oscarg798.remembrall.auth.Session
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val session: Session) {

    suspend fun execute() {
        session.logout()
    }
}
