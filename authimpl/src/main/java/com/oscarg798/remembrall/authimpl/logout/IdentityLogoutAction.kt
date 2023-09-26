package com.oscarg798.remembrall.authimpl.logout

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.oscarg798.remembrall.auth.LogoutAction
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.gmstaskutils.toSuspend
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class IdentityLogoutAction @Inject constructor(
    @ApplicationContext private val context: Context
) : LogoutAction {

    override suspend fun invoke(sessionState: Session.State.LoggedIn) {
        Identity.getSignInClient(context).signOut().toSuspend {
            IllegalStateException("Can not logout from Identity", it)
        }
    }
}
