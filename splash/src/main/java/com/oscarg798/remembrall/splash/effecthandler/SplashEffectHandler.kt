package com.oscarg798.remembrall.splash.effecthandler

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.MobiusCoroutines
import com.oscarg798.remembrall.splash.domain.Effect
import com.oscarg798.remembrall.splash.domain.Event
import com.oscarg798.remembrall.splash.usecase.IsUserLoggedIn
import com.spotify.mobius.Connectable
import javax.inject.Inject

internal class SplashEffectHandler @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedIn,
    private val coroutinesContextProvider: CoroutineContextProvider
) : EffectHandlerProvider<Effect, Event> {

    override fun provide(uiEffectConsumer: EffectConsumer<Effect>): Connectable<Effect, Event> {
        return MobiusCoroutines.subtypeEffectHandler<Effect, Event>()
            .addFunction<Effect.CheckSessionState> { isUserLoggedIn() }
            .addConsumer<Effect.UIEffect.NavigateToLogin>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.NavigateToHome>(uiEffectConsumer)
            .build(coroutinesContextProvider.computation)
    }
}