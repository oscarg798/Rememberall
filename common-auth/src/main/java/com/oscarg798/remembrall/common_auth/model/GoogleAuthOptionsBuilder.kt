package com.oscarg798.remembrall.common_auth.model

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthOptionsBuilder @Inject constructor() {

    fun buildFromAuthOptions(authOptions: AuthOptions): GoogleSignInOptions {
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

    fun buildSignInRequest(authOptions: AuthOptions): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(authOptions.clientId)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build();
    }
}
