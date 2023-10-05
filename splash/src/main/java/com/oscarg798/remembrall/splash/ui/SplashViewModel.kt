package com.oscarg798.remembrall.splash.ui

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.splash.domain.Effect
import com.oscarg798.remembrall.splash.domain.Event
import com.oscarg798.remembrall.splash.domain.Model
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.spotify.mobius.First
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>,
    coroutineContextProvider: CoroutineContextProvider
) : MobiusViewModel<Model, Event, Effect>(
    initialModel = Model(loading = true),
    init = { First.first(it, setOf(Effect.CheckSessionState)) },
    loopInjector = loopInjector,
    coroutineContextProvider = coroutineContextProvider
) {

    val uiEffect: Flow<Effect.UIEffect> = uiEffectState.asSharedFlow()
}
