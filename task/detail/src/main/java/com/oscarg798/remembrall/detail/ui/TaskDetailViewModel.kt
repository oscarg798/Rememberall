package com.oscarg798.remembrall.detail.ui

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.detail.domain.Model
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.spotify.mobius.First.first
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class TaskDetailViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    loopInjector: LoopInjector<Model, Event, Effect>,
    coroutineContextProvider: CoroutineContextProvider,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>
) : MobiusViewModel<Model, Event, Effect>(
    initialModel = Model(taskId),
    init = { first(it, setOf(Effect.GetTask(it.taskId))) },
    loopInjector = loopInjector,
    coroutineContextProvider = coroutineContextProvider
) {

    val uiEffects = uiEffectState.asSharedFlow()

    @AssistedFactory
    interface Factory {

        fun create(taskId: String): TaskDetailViewModel
    }
}