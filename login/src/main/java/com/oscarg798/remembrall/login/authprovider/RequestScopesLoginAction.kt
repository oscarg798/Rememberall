package com.oscarg798.remembrall.login.authprovider

import android.app.Activity
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.oscarg798.remembrall.auth.LoginAction
import com.oscarg798.remembrall.common.ActivityProvider
import com.oscarg798.remembrall.common.toSuspend
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import  com.oscarg798.remembrall.auth.ExternalAuthProvider.SignInRequestResult
import javax.inject.Inject

internal class RequestScopesLoginAction @Inject constructor(
    private val authOptions: AuthOptions,
    private val activityProvider: ActivityProvider
) : LoginAction {

    override suspend fun invoke(signInResult: SignInRequestResult) {
        val activity = activityProvider.provide()
            ?: throw IllegalStateException("activity required for RequestScopesAction")

        val scopes = authOptions.scopes.map {
            Scope(it)
        }

        val authorizationRequest =
            AuthorizationRequest.builder().setRequestedScopes(scopes).build()

        val applicationContext = activity.applicationContext

        val identifyResult = Identity.getAuthorizationClient(applicationContext)
            .authorize(authorizationRequest)
            .toSuspend()

        if (!identifyResult.isSuccessful) {
            throw identifyResult.exception
                ?: IllegalStateException("Authorization Error no successful")
        }

        // If has resolution we need to display proper UI
        if (identifyResult.result.hasResolution()) {
            val result = activity.launchForResult(
                identifyResult.result!!.pendingIntent!!.intentSender,
                ScopeAuthorizationRequestCode
            )

            if (result.resultCode != Activity.RESULT_OK) {
                throw IllegalStateException("Authorization Result no okay ${result.resultCode}")
            }
        }
    }
}

private const val ScopeAuthorizationRequestCode = "ScopeAuthorization"