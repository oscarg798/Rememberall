package com.oscarg798.remembrall.common_auth.model

data class AuthOptions(
    val requestServerAuth: Boolean,
    val requestProfileInfo: Boolean,
    val clientId: String,
    val scopes: Collection<String>
)
