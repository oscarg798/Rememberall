package com.oscarg798.remembrall.login.domain

import com.oscarg798.remembrall.common_auth.exception.AuthException

internal typealias UpComing = Pair<Model, Set<Effect>>

internal fun update(
    model: Model,
    event: Event
): UpComing = when (event) {
    Event.OnBackPresses -> onBackPressed(model)
    Event.SignIn -> onSignIn(model)
    is Event.OnLoginError -> onLoginError(model, event)
    Event.OnSignedIn -> onSignedIn(model)
    is Event.OnExternalSignInOptionsFound -> onExternalOptionsFound(model, event)
    is Event.OnExternalSignInFinished -> onExternalSignInFinished(model, event)
}

private fun onExternalSignInFinished(model: Model, event: Event.OnExternalSignInFinished): UpComing =
    Pair(model, setOf(Effect.FinishLogin(event.result.token))).also {
        System.err.println("External Sign in finished finishing login")
    }

private fun onExternalOptionsFound(
    model: Model,
    event: Event.OnExternalSignInOptionsFound
): UpComing {
    return Pair(model, setOf(Effect.RequestExternalAuth(event.options, event.signInRequest)))
}

private fun onSignedIn(model: Model): UpComing {
    System.err.println("Going to home")
    return Pair(model.copy(loading = false), setOf(Effect.UIEffect.NavigateToHome))
}

private fun onBackPressed(model: Model) = Pair(model, setOf(Effect.UIEffect.NavigateBack))
private fun onSignIn(model: Model) = Pair(
    if (model.loading) {
        model
    } else {
        model.copy(loading = true)
    }, setOf(Effect.GetExternalSigningCredentials)
)

private fun onLoginError(model: Model, event: Event.OnLoginError): UpComing {
    System.err.println("ERROR LOGIN$$$$$$$$$$$$$$$$$$$$$$$$$$")
    System.err.println("ERROR LOGIN ${event.error.message}")
    System.err.println("ERROR LOGIN ${event.error.stackTrace}")
    System.err.println("ERROR LOGIN ${event.error}")
    System.err.println("ERROR LOGIN$$$$$$$$$$$$$$$$$$$$$$$$$$")
    return Pair(
        model.copy(loading = false), setOf(
            when (event.error) {
                is AuthException -> Effect.UIEffect.ShowErrorMessage.LoginError
                else -> Effect.UIEffect.ShowErrorMessage.UnknownError
            }
        )
    )
}