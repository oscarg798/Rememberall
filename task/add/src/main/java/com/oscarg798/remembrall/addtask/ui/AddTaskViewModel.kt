package com.oscarg798.remembrall.addtask.ui

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Model
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.spotify.mobius.First
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
internal class AddTaskViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>,
    coroutineContextProvider: CoroutineContextProvider,
) : MobiusViewModel<Model, Event, Effect>(
    initialModel = Model(),
    init = { First.first(
            it,
            setOf(Effect.GetAvailableTaskPriorities(it.priority))
        )
    },
    loopInjector = loopInjector, coroutineContextProvider = coroutineContextProvider,
) {


    val uiEffect: Flow<Effect.UIEffect> = uiEffectState.asSharedFlow()

}
