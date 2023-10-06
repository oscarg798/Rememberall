package com.oscarg798.remembrall.addtask.ui

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.task.addroute.AddRoute
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Model
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.viewmodelutils.MobiusViewModel
import com.spotify.mobius.First
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


internal class AddTaskViewModel @AssistedInject constructor(
    @Assisted private val taskId: String,
    loopInjector: LoopInjector<Model, Event, Effect>,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>,
    coroutineContextProvider: CoroutineContextProvider,
) : MobiusViewModel<Model, Event, Effect>(
    initialModel = Model(taskId = taskId),
    init = {
        First.first(
            it,
            setOf(
                Effect.GetAvailableTaskPriorities(it.priority)
            ) + if (taskId != AddRoute.None) {
                setOf(Effect.LoadTask(taskId))
            } else {
                emptySet()
            }
        )
    },
    loopInjector = loopInjector, coroutineContextProvider = coroutineContextProvider,
) {


    val uiEffect: Flow<Effect.UIEffect> = uiEffectState.asSharedFlow()

    @AssistedFactory
    interface Factory {

        fun create(taskId: String): AddTaskViewModel
    }
}
