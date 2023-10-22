package com.oscarg798.remembrall.authimpl.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.auth.ExternalAuthProvider
import com.oscarg798.remembrall.auth.LoginAction
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.gmstaskutils.toSuspend
import javax.inject.Inject
import kotlinx.coroutines.withContext

// TODO("move this to google sign in module or FirebaseAuth
internal class FinishLoginAction @Inject constructor(
    private val session: Session,
    private val firebaseAuth: FirebaseAuth,
    private val coroutinesContextProvider: CoroutineContextProvider
) : LoginAction {

    override suspend fun invoke(result: ExternalAuthProvider.SignInRequestResult) {
        withContext(coroutinesContextProvider.io) {
            val credential = GoogleAuthProvider.getCredential(result.token, null)

            val result = firebaseAuth.signInWithCredential(credential).toSuspend()
            if(!result.isSuccessful){
                error("Error logging in ${result.exception?.message}")
            }

            session.silentLoginIng()
        }
    }
}
