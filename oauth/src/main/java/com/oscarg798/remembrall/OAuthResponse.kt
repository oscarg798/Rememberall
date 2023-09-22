package com.oscarg798.remembrall


data class OAuthResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String?,
    val idToken: String?,
    val issuedOn: Long
)