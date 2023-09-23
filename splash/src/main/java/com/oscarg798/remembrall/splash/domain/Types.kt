package com.oscarg798.remembrall.splash.domain

import com.oscarg798.remembrall.auth.Session

data class Model(val loading: Boolean = false)

sealed interface Event {

    data class OnSessionStateObtained(val state: Session.State) : Event
}

sealed interface Effect {

    object CheckSessionState : Effect

    sealed interface UIEffect: Effect {
        object NavigateToLogin : UIEffect
        data class NavigateToHome(val x: Boolean) : UIEffect
    }
}