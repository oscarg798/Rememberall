package com.oscarg798.remembrall.common.auth

import com.oscarg798.remembrall.common.model.User


interface AuthRepository {

    fun getSignedInUser(): User

    suspend fun silentAuth(): User

    suspend fun logout()

    fun isUserLoggedIn(): Boolean

    suspend fun finishLogIn()
}
