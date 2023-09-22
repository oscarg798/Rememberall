package com.oscarg798.remembrall.common_auth.network.model

import com.oscarg798.remembrall.user.User

data class SignInDto(
    val user: com.oscarg798.remembrall.user.User,
    val serverAuthToken: String?,
    val token: String
)
