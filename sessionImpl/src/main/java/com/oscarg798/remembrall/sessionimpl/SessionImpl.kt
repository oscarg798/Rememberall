package com.oscarg798.remembrall.sessionimpl

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.user.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

internal class SessionImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coroutineContextProvider: CoroutineContextProvider
) : Session {
    override suspend fun getLoggedInState(): Session.State =
        withContext(coroutineContextProvider.io) {
            GoogleSignIn.getLastSignedInAccount(context)?.toUser()?.let {
                Session.State.LoggedIn(it)
            } ?: Session.State.NoLogged
        }

    private fun GoogleSignInAccount.toUser() = User(
        getUserName(),
        email ?: throw IllegalStateException("Email request in account $id")
    )

    override fun streamLoggedInState(): Flow<Session.State> = flow {
        while (true) {
            emit(getLoggedInState())
            delay(UpdateThrottling)
            yield()
        }
    }.flowOn(coroutineContextProvider.computation)

    private fun GoogleSignInAccount.getUserName() = when {
        this.displayName != null -> this.displayName!!
        this.givenName != null && this.familyName != null -> "${this.givenName} ${this.familyName}"
        this.givenName != null -> this.givenName!!
        else -> "Unknown"
    }
}

private const val UpdateThrottling = 1_000L
