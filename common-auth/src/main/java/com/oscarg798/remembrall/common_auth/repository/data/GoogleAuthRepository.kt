package com.oscarg798.remembrall.common_auth.repository.data

import arrow.core.getOrHandle
import com.oscarg798.remembrall.common_auth.model.User
import com.oscarg798.remembrall.common_auth.network.restclient.ExternalSignInClient
import com.oscarg798.remembrall.common_auth.repository.domain.AuthRepository
import javax.inject.Inject

class GoogleAuthRepository @Inject constructor(
    private val externalSignInClient: ExternalSignInClient
) : AuthRepository {

    override fun getSignedInUser(): User {
        return externalSignInClient.getSignedInUser().getOrHandle { error ->
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
}
