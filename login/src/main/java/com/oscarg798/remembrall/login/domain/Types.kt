package com.oscarg798.remembrall.login.domain

import com.oscarg798.remembrall.auth.ExternalAuthProvider

internal data class Model(
    val isLoggedIn: Boolean = false,
    val loading: Boolean = false
)

internal sealed interface Event {

    object SignIn : Event
    object OnBackPresses : Event
    object OnSignedIn : Event
    data class OnLoginError(val error: Exception) : Event
    data class OnExternalSignInFinished(
        val result: ExternalAuthProvider.SignInRequestResult
    ) : Event
}

internal sealed interface Effect {
    data class FinishLogin(val idToken: String) : Effect
    object RequestExternalAuth : Effect
    sealed interface UIEffect : Effect {
        object NavigateToHome : UIEffect
        object NavigateBack : UIEffect

        sealed interface ShowErrorMessage : UIEffect {
            object LoginError : ShowErrorMessage
            object UnknownError : ShowErrorMessage
        }
    }
}
