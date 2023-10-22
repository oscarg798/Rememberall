package com.oscarg798.remembrall.widgetimpl

import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.rxutils.SchedulersProvider
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import com.oscarg798.remembrall.widgetimpl.domain.Model
import com.oscarg798.remembrall.widgetimpl.domain.update
import com.spotify.mobius.Init
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.rx3.SchedulerWorkRunner
import javax.inject.Inject

internal class LoopInjectorImpl @Inject constructor(
    private val schedulerProvider: SchedulersProvider,
    private val uiEffectConsumer: EffectConsumer<Effect>,
    private val widgetEffectHandler: EffectHandlerProvider<Effect, Event>,
) : LoopInjector<Model, Event, Effect> {

    override fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event> {
        return Mobius.controller(
            Mobius.loop(
                ::update,
                widgetEffectHandler.provide(uiEffectConsumer)
            ).eventRunner {
                SchedulerWorkRunner(schedulerProvider.computation)
            }.effectRunner {
                SchedulerWorkRunner(schedulerProvider.computation)
            },
            initialModel,
            init,
        )
    }
}