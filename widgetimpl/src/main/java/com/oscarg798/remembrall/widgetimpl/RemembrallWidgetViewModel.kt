package com.oscarg798.remembrall.widgetimpl

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import com.oscarg798.remembrall.widgetimpl.domain.Model
import com.spotify.mobius.First
import com.spotify.mobius.Init
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
internal class RemembrallWidgetViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    coroutineContextProvider: CoroutineContextProvider,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>
) : MobiusViewModel<Model, Event, Effect>(
    Model(), Init {
        First.first(it, setOf(Effect.GetSessionState))
    },
    loopInjector,
    coroutineContextProvider
) {
    val uiEffects = uiEffectState.asSharedFlow()

}