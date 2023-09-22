package com.oscarg798.remembrall

interface OAuthClient  {

    suspend fun auth(authCode: String): OAuthResponse
}