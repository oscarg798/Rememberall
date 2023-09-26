package com.oscarg798.remembrall.authimpl

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.oscarg798.remembrall.android.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.auth.AuthOptions
import javax.inject.Inject

class GoogleAuthOptionsBuilderImpl @Inject constructor() : GoogleAuthOptionsBuilder {

    override fun invoke(authOptions: AuthOptions): GoogleSignInOptions {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()

        if (authOptions.requestIdToken) {
            builder.requestIdToken(authOptions.clientId)
        }

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
