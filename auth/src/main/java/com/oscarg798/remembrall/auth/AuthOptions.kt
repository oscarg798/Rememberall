package com.oscarg798.remembrall.auth

data class AuthOptions(
    val requestServerAuth: Boolean,
    val requestProfileInfo: Boolean,
    val requestIdToken: Boolean,
    val clientId: String,
    val scopes: Collection<String>
)