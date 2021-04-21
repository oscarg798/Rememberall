package com.oscarg798.remembrall.common.network.dto

import com.oscarg798.remembrall.common.model.User

data class SignInDto(val user: User, val serverAuthToken: String?)
