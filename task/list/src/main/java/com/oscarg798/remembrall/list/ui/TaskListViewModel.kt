package com.oscarg798.remembrall.list.ui

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.domain.model.Model
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.spotify.mobius.First.first
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
internal class TaskListViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>,
    coroutineContextProvider: CoroutineContextProvider,
) : MobiusViewModel<Model, Event, Effect>(
    initialModel = Model(),
    init = { first(it, setOf(Effect.GetTasks)) },
    loopInjector = loopInjector,
    coroutineContextProvider = coroutineContextProvider
){
    val uiEffects = uiEffectState.asSharedFlow()
}