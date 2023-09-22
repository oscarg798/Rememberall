package com.oscarg798.remembrall.login.authprovider

import android.app.Activity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.oscarg798.remembrall.auth.ExternalAuthProvider
import com.oscarg798.remembrall.auth.LoginAction
import com.oscarg798.remembrall.common.ActivityProvider
import com.oscarg798.remembrall.common.toSuspend
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import javax.inject.Inject
import java.lang.ref.WeakReference

internal class OneTapAuthProvider @Inject constructor(
    activityProvider: ActivityProvider,
    private val authOptions: AuthOptions,
    private val loginActions: Set<@JvmSuppressWildcards LoginAction>,
) : ExternalAuthProvider {

    private val activity by lazy {
        WeakReference(activityProvider.provide())
    }

    private val oneTapClient by lazy {
        Identity.getSignInClient(activity.get()!!);
    }

    override suspend fun signIn(): ExternalAuthProvider.SignInRequestResult {
        val tapSignInResult = oneTapClient.beginSignIn(buildSignInRequest(authOptions)).toSuspend()

        if (!tapSignInResult.isSuccessful) {

            throw tapSignInResult.exception
                ?: IllegalStateException("Sign in failure result no successful")
        }

        val signInResult = activity.get()?.launchForResult(
            intentSender = tapSignInResult.result.pendingIntent.intentSender,
            requestCode = TapSignInRequestCode
        ) ?: throw IllegalStateException("To SignIn we require an Activity")

        if (signInResult.resultCode != Activity.RESULT_OK) {
            throw IllegalStateException("Result no okay ${signInResult.resultCode}")
        }

        val credential = oneTapClient.getSignInCredentialFromIntent(signInResult.data)

        val result = ExternalAuthProvider.SignInRequestResult(
            username = credential.id,
            token = credential.googleIdToken!!
        )

        loginActions.forEach { action -> action(result) }

        return result
    }

    private fun buildSignInRequest(authOptions: AuthOptions): BeginSignInRequest {
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
            .build()
    }
}

private const val TapSignInRequestCode = "TapSignIn"