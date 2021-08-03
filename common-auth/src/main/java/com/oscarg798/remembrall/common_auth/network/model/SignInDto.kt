package com.oscarg798.remembrall.common_auth.network.model

import com.oscarg798.remembrall.common_auth.model.User

data class SignInDto(val user: User, val serverAuthToken: String?)
