package com.oscarg798.remembrall.list.ui

import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.domain.model.Model
import com.oscarg798.remembrall.list.domain.update
import com.oscarg798.remembrall.list.effecthandler.TaskListEffectHandler
import com.oscarg798.remembrall.list.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.rxutils.SchedulersProvider
import com.spotify.mobius.Init
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.rx3.SchedulerWorkRunner
import javax.inject.Inject

internal class LoopInjectorImpl @Inject constructor(
    private val uiEffectConsumer: UIEffectConsumer,
    private val schedulersProvider: SchedulersProvider,
    private val listEffectHandler: TaskListEffectHandler,
): LoopInjector<Model, Event, Effect> {

    override fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event> {
        return Mobius.controller(
            Mobius.loop(
                ::update,
                listEffectHandler.provide(uiEffectConsumer)
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