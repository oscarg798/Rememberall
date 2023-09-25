package com.oscarg798.remembrall.auth

import com.oscarg798.remembrall.user.User
import kotlinx.coroutines.flow.Flow

interface Session {

    suspend fun getLoggedInState(): State

    fun streamLoggedInState(): Flow<State>

    suspend fun logout()

    suspend fun silentLoginIng(): Session.State.LoggedIn

    sealed interface State {
        object NoLogged : State
        data class LoggedIn(val user: User) : State
    }
}