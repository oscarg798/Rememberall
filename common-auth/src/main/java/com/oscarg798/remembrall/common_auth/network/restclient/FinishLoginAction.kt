package com.oscarg798.remembrall.common_auth.network.restclient

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.auth.ExternalAuthProvider.SignInRequestResult
import com.oscarg798.remembrall.auth.LoginAction
import com.oscarg798.remembrall.common.toSuspend
import com.oscarg798.remembrall.common_auth.exception.AuthException
import javax.inject.Inject
import kotlinx.coroutines.withContext

// TODO("move this to google sign in module or FirebaseAuth
internal class FinishLoginAction @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val coroutinesContextProvider: CoroutineContextProvider
) : LoginAction {

    override suspend fun invoke(result: SignInRequestResult) {
        withContext(coroutinesContextProvider.io) {
            val credential = GoogleAuthProvider.getCredential(result.token, null)

            firebaseAuth.signInWithCredential(credential).toSuspend {
                AuthException.ErrorFinishingLogOutProcessWithExternalAuthenticators(it)
            }
        }
    }

}