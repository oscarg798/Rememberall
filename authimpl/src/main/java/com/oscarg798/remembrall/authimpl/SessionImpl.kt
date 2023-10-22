package com.oscarg798.remembrall.authimpl

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.android.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.auth.AuthOptions
import com.oscarg798.remembrall.auth.LogoutAction
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.gmstaskutils.toSuspend
import com.oscarg798.remembrall.user.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

internal class SessionImpl @Inject constructor(
    private val authOptions: AuthOptions,
    @ApplicationContext private val context: Context,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder,
    private val logoutActions: Set<@JvmSuppressWildcards LogoutAction>,
) : Session {
    override suspend fun getSessionState(): Session.State =
        withContext(coroutineContextProvider.io) {
            GoogleSignIn.getLastSignedInAccount(context)?.toUser()?.let {
                Session.State.LoggedIn(it)
            } ?: Session.State.NoLogged
        }

    private fun GoogleSignInAccount.toUser() = User(
        name = getUserName(),
        email = email ?: throw NullPointerException("Email not found in account $id"),
        serverAuthToken = serverAuthCode,
        token = idToken ?: throw NullPointerException("idToken not found in account $id")
    )

    override fun streamLoggedInState(): Flow<Session.State> = flow {
        while (true) {
            emit(getSessionState())
            delay(UpdateThrottling)
            yield()
        }
    }.distinctUntilChanged()
        .flowOn(coroutineContextProvider.computation)

    override suspend fun silentLoginIng(): Session.State.LoggedIn {
        val signInResult = withContext(coroutineContextProvider.io) {
            GoogleSignIn.getClient(
                context,
                googleAuthOptionsBuilder(authOptions)
            ).silentSignIn().toSuspend()
        }

        if (!signInResult.isSuccessful) {
            throw IllegalStateException("Can not perform silent log in", signInResult.exception)
        }
        return Session.State.LoggedIn(signInResult.result.toUser())
    }

    override suspend fun logout() {
        val sessionState = getSessionState() as? Session.State.LoggedIn
            ?: throw IllegalStateException("To perform a logout user must be logged in first")
        logoutActions.forEach { it(sessionState) }
    }

    private fun GoogleSignInAccount.getUserName() = when {
        this.displayName != null -> this.displayName!!
        this.givenName != null && this.familyName != null -> "${this.givenName} ${this.familyName}"
        this.givenName != null -> this.givenName!!
        else -> "Unknown"
    }
}

private const val UpdateThrottling = 1_000L
