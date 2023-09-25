package com.recipegenius.authimpl.logout

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.oscarg798.remembrall.android.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.auth.AuthOptions
import com.oscarg798.remembrall.auth.LogoutAction
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.gmstaskutils.toSuspend
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class GoogleSignInLogoutAction @Inject constructor(
    private val authOptions: AuthOptions,
    @ApplicationContext private val context: Context,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder
) : LogoutAction {

    override suspend fun invoke(session: Session.State.LoggedIn) {
        GoogleSignIn.getClient(
            context,
            googleAuthOptionsBuilder(authOptions)
        ).signOut().toSuspend {
            IllegalStateException("Can not logout from GoogleSign In", it)
        }
    }
}