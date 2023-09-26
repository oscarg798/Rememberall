package com.oscarg798.remembrall.splash.domain

import com.oscarg798.remembrall.auth.Session
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next

internal typealias UpComing = Next<Model, Effect>

internal fun update(
    model: Model,
    event: Event
): UpComing = when (event) {
    is Event.OnSessionStateObtained -> onSessionStateObtained(model, event)
}

private fun onSessionStateObtained(model: Model, event: Event.OnSessionStateObtained): UpComing {
    val effects = setOf(
        if (event.state is Session.State.LoggedIn) {
            Effect.UIEffect.NavigateToHome(true)
        } else {
            Effect.UIEffect.NavigateToLogin
        }
    )

    return if (model.loading) {
        next(model.copy(loading = false), effects)
    } else {
        dispatch(effects)
    }
}
