package com.oscarg798.remembrall.common_auth.repository.domain

import com.oscarg798.remembrall.common_auth.model.User

interface AuthRepository {

    fun getSignedInUser(): User

    suspend fun silentAuth(): User

    suspend fun logout()
}
