package com.oscarg798.remembrall.login.effecthandler

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.MobiusCoroutines
import com.spotify.mobius.Connectable
import javax.inject.Inject

internal interface LoginEffectHandlerProvider : EffectHandlerProvider<Effect, Event>

internal class LoginEffectHandlerProviderImpl @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val finishLogInEffectHandler: FinishLogInEffectHandler,
    private val requestExternalAuthEffectHandler: RequestExternalAuthEffectHandler,
) : LoginEffectHandlerProvider {

    override fun provide(uiEffectConsumer: EffectConsumer<Effect>): Connectable<Effect, Event> {
        return MobiusCoroutines.subtypeEffectHandler<Effect, Event>()
            .addFunction(finishLogInEffectHandler)
            .addFunction(requestExternalAuthEffectHandler)
            .addConsumer<Effect.UIEffect.NavigateToHome>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.ShowErrorMessage>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.NavigateBack>(uiEffectConsumer)
            .build(coroutineContextProvider.computation)
    }
}
