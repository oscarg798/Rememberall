package com.oscarg798.remembrall.auth

interface ExternalAuthProvider {

    suspend fun signIn(): SignInRequestResult

    data class SignInRequestResult(
        val username: String,
        val token: String
    )
}
