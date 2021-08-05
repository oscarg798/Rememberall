package com.oscarg798.remembrall.common_auth.model

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthOptionsBuilder @Inject constructor() {

    fun buildFromAuthOptions(authOptions: AuthOptions): GoogleSignInOptions {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()

        if(authOptions.requestIdToken){
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
