package com.oscarg798.remembrall.common.repository.domain

import com.oscarg798.remembrall.common.model.User

interface AuthRepository {

    fun getSignedInUser(): User

    suspend fun silentAuth(): User

    suspend fun logout()
}
