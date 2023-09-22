package com.oscarg798.remembrall.common_auth.repository.data

import arrow.core.getOrElse
import arrow.core.getOrHandle
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.common.auth.AuthRepository
import com.oscarg798.remembrall.user.User
import com.oscarg798.remembrall.common_auth.network.restclient.ExternalSignInClient
import javax.inject.Inject
import kotlinx.coroutines.withContext

class GoogleAuthRepository @Inject constructor(
    private val externalSignInClient: ExternalSignInClient,
    private val coroutineContextProvider: CoroutineContextProvider
) : AuthRepository {

    override suspend fun getSignedInUser(): User = withContext(coroutineContextProvider.io) {
        externalSignInClient.getSignedInUser().getOrHandle { error ->
            throw error
        }.user
    }

    override suspend fun silentAuth(): User {
        return externalSignInClient.silentSignIn().getOrHandle { error ->
            throw error
        }.user
    }

    override suspend fun logout() {
        externalSignInClient.logout()
    }

    override fun isUserLoggedIn(): Boolean {
        return externalSignInClient.isUserLoggedIn()
            .map { true }
            .getOrElse { false }
    }

    override suspend fun finishLogIn(idToken: String) {
        externalSignInClient.finishLogIn(idToken)
    }
}
