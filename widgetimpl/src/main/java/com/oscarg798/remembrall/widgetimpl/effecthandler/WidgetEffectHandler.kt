package com.oscarg798.remembrall.widgetimpl.effecthandler

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.MobiusCoroutines
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import com.oscarg798.remembrall.widgetimpl.usecase.ForceWidgetRefresh
import com.oscarg798.remembrall.widgetimpl.usecase.GetSessionState
import com.oscarg798.remembrall.widgetimpl.usecase.GetTasks
import com.spotify.mobius.Connectable
import javax.inject.Inject

internal class WidgetEffectHandler @Inject constructor(
    private val getTasks: GetTasks,
    private val getSessionState: GetSessionState,
    private val forceWidgetRefresh: ForceWidgetRefresh,
    private val coroutineContextProvider: CoroutineContextProvider,
): EffectHandlerProvider<Effect, Event> {

    override fun provide(uiEffectConsumer: EffectConsumer<Effect>): Connectable<Effect, Event> {
        return MobiusCoroutines.subtypeEffectHandler<Effect, Event>()
            .addFunction(getSessionState)
            .addConsumer(forceWidgetRefresh)
            .addEffectHandler(Effect.GetTasks::class, getTasks::invoke)
            .addConsumer<Effect.UIEffect.NavigateToLogin>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.NavigateToDetail>(uiEffectConsumer)
            .build(coroutineContextProvider.computation)
    }
}