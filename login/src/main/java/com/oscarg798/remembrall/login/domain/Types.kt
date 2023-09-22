package com.oscarg798.remembrall.login.domain

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
    data class OnExternalSignInOptionsFound(
        val options: GoogleSignInOptions,
        val signInRequest: BeginSignInRequest
    ) : Event

    data class OnExternalSignInFinished(val result: ExternalAuthProvider.SignInRequestResult) : Event
}

internal sealed interface Effect {
    object GetExternalSigningCredentials : Effect
    data class FinishLogin(val idToken: String) : Effect
    data class RequestExternalAuth(
        val options: GoogleSignInOptions,
        val signInRequest: BeginSignInRequest
    ) : Effect
    sealed interface UIEffect : Effect {
        object NavigateToHome : UIEffect
        object NavigateBack : UIEffect

        sealed interface ShowErrorMessage : UIEffect {
            object LoginError : ShowErrorMessage
            object UnknownError : ShowErrorMessage
        }
    }
}