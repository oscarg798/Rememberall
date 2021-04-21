package com.oscarg798.remembrall.common

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.oscarg798.remembrall.common.model.AuthOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthOptionsBuilder @Inject constructor() {

    fun buildFromAuthOptions(authOptions: AuthOptions): GoogleSignInOptions {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

        if (authOptions.requestProfileInfo) {
            builder.requestProfile()
        }

        if (authOptions.requestServerAuth) {
            builder.requestServerAuthCode(authOptions.clientId)
        }

        authOptions.scopes.forEach {
            builder.requestScopes(Scope(it))
        }

        return builder.build()
    }
}
