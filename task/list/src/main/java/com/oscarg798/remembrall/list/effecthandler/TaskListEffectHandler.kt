package com.oscarg798.remembrall.list.effecthandler

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.usecase.GetInitialIndexPosition
import com.oscarg798.remembrall.list.usecase.GetTaskGrouped
import com.oscarg798.remembrall.list.usecase.GetTaskOptions
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.MobiusCoroutines
import com.spotify.mobius.Connectable
import javax.inject.Inject

internal class TaskListEffectHandler @Inject constructor(
    private val getTaskGrouped: GetTaskGrouped,
    private val getTaskOptions: GetTaskOptions,
    private val getInitialIndexPosition: GetInitialIndexPosition,
    private val coroutineContextProvider: CoroutineContextProvider,
) : EffectHandlerProvider<Effect, Event> {

    override fun provide(uiEffectConsumer: EffectConsumer<Effect>): Connectable<Effect, Event> {
        return MobiusCoroutines.subtypeEffectHandler<Effect, Event>()
            .addFunction(getTaskOptions)
            .addFunction(getInitialIndexPosition)
            .addConsumer<Effect.UIEffect.ShowOptions>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.ScrollToItem>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.NavigateToAdd>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.NavigateToDetail>(uiEffectConsumer)
            .addEffectHandler(Effect.GetTasks::class, getTaskGrouped::invoke)
            .build(coroutineContextProvider.computation)
    }
}