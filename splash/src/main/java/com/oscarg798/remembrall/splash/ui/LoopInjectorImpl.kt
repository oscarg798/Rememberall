package com.oscarg798.remembrall.splash.ui

import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.rxutils.SchedulersProvider
import com.oscarg798.remembrall.splash.domain.Effect
import com.oscarg798.remembrall.splash.domain.Event
import com.oscarg798.remembrall.splash.domain.Model
import com.oscarg798.remembrall.splash.domain.update
import com.oscarg798.remembrall.splash.effecthandler.UIEffectConsumer
import com.spotify.mobius.Init
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.rx3.SchedulerWorkRunner
import javax.inject.Inject

internal class LoopInjectorImpl @Inject constructor(
    private val uiEffectConsumer: UIEffectConsumer,
    private val effectHandlerProvider: EffectHandlerProvider<Effect, Event>,
    private val schedulersProvider: SchedulersProvider
) : LoopInjector<Model, Event, Effect> {

    override fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event> {
        return Mobius.controller(
            Mobius.loop(
                ::update,
                effectHandlerProvider.provide(uiEffectConsumer)
            ).eventRunner {
                SchedulerWorkRunner(schedulersProvider.computation)
            }.effectRunner {
                SchedulerWorkRunner(schedulersProvider.computation)
            },
            initialModel,
            init,
        )
    }
}
