package com.oscarg798.remembrall.common_auth.network.restclient

import arrow.core.Either
import com.oscarg798.remembrall.common_auth.exception.AuthException
import com.oscarg798.remembrall.common_auth.model.User
import com.oscarg798.remembrall.common_auth.network.model.SignInDto

/**
 * This interface want to encapsulate the logic
 * to get sign in an [User] from an external provider
 * such us Google, Facebook, etc..
 *
 * We used [Either] to put in the contract the type
 * of the interface that this client should provide
 *
 */
interface ExternalSignInClient {

    suspend fun silentSignIn(): Either<AuthException, SignInDto>

    fun getSignedInUser(): Either<AuthException, SignInDto>

    suspend fun logout()
}
