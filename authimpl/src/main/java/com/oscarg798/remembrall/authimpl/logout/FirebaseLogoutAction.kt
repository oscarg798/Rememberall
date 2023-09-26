package com.oscarg798.remembrall.authimpl.logout

import com.google.firebase.auth.FirebaseAuth
import com.oscarg798.remembrall.auth.LogoutAction
import com.oscarg798.remembrall.auth.Session
import javax.inject.Inject

internal class FirebaseLogoutAction @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : LogoutAction {

    override suspend fun invoke(sessionState: Session.State.LoggedIn) {
        firebaseAuth.signOut()
    }
}
