package com.oscarg798.remembrall.login.effecthandler

import com.oscarg798.remembrall.auth.ExternalAuthProvider
import com.oscarg798.remembrall.login.domain.Effect.RequestExternalAuth
import com.oscarg798.remembrall.login.domain.Event
import javax.inject.Inject

internal class RequestExternalAuthEffectHandler @Inject constructor(
    private val externalAuthProvider: ExternalAuthProvider
) : EffectTransformer<RequestExternalAuth, Event> {

    override suspend fun invoke(effect: RequestExternalAuth): Event {
        return runCatching { externalAuthProvider.signIn() }.fold({
            Event.OnExternalSignInFinished(it)
        }, {
            if (it !is Exception) throw it
            Event.OnLoginError(it)
        })
    }
}
