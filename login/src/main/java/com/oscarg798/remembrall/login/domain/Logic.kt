package com.oscarg798.remembrall.login.domain

import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next

internal typealias UpComing = Next<Model, Effect>

internal fun update(
    model: Model,
    event: Event
): UpComing = when (event) {
    Event.OnBackPresses -> onBackPressed()
    Event.SignIn -> onSignIn(model)
    is Event.OnLoginError -> onLoginError(model)
    Event.OnSignedIn -> onSignedIn(model)
    is Event.OnExternalSignInFinished -> onExternalSignInFinished(event)
}

private fun onExternalSignInFinished(
    event: Event.OnExternalSignInFinished
): UpComing = dispatch(setOf(Effect.FinishLogin(event.result.token)))

private fun onSignedIn(model: Model): UpComing {
    val effects = setOf(Effect.UIEffect.NavigateToHome)
    return if (model.loading) {
        next(model.copy(loading = false), effects)
    } else {
        dispatch(effects)
    }
}

private fun onBackPressed(): UpComing {
    return dispatch(setOf(Effect.UIEffect.NavigateBack))
}

private fun onSignIn(model: Model): UpComing {
    val effects = setOf(Effect.RequestExternalAuth)
    return if (model.loading) {
        dispatch(effects)
    } else {
        next(
            model.copy(
                loading = true,
            ),
            effects
        )
    }
}

private fun onLoginError(model: Model): UpComing {
    val effects = setOf(Effect.UIEffect.ShowErrorMessage.LoginError)

    return if (model.loading) {
        next(model.copy(loading = false), effects)
    } else {
        dispatch(effects)
    }
}
