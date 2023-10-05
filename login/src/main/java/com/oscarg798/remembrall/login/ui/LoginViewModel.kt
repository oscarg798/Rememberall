package com.oscarg798.remembrall.login.ui

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.login.domain.Model
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.spotify.mobius.First
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    coroutineContextProvider: CoroutineContextProvider,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>
) : MobiusViewModel<Model, Event, Effect>(
    initialModel = Model(),
    init = {
        First.first(it)
    },
    loopInjector = loopInjector,
    coroutineContextProvider = coroutineContextProvider
) {

    val uiEffect: Flow<Effect.UIEffect> = uiEffectState.asSharedFlow()

}
