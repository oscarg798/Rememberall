package com.oscarg798.remembrall.detail.effecthandler

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.detail.usecase.GetTask
import com.oscarg798.remembrall.detail.usecase.MarkTaskAsCompleted
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.MobiusCoroutines
import com.spotify.mobius.Connectable
import javax.inject.Inject

internal class TaskDetailEffectHandler @Inject constructor(
    private val getTask: GetTask,
    private val markTaskAsCompleted: MarkTaskAsCompleted,
    private val coroutineContextProvider: CoroutineContextProvider,
) : EffectHandlerProvider<Effect, Event> {

    override fun provide(uiEffectConsumer: EffectConsumer<Effect>): Connectable<Effect, Event> {
        return MobiusCoroutines.subtypeEffectHandler<Effect, Event>()
            .addFunction(markTaskAsCompleted)
            .addEffectHandler(Effect.GetTask::class, getTask::invoke)
            .addConsumer<Effect.UIEffect.ShowError>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.CloseScreen>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.NavigateToEdit>(uiEffectConsumer)
            .build(coroutineContextProvider.computation)
    }
}