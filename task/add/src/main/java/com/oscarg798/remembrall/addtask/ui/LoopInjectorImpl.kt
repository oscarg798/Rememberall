package com.oscarg798.remembrall.addtask.ui

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Model
import com.oscarg798.remembrall.addtask.domain.update
import com.oscarg798.remembrall.addtask.effecthandler.AddTaskEffectHandler
import com.oscarg798.remembrall.addtask.effecthandler.UIEffectConsumer
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
    private val addTaskEffectHandler: AddTaskEffectHandler,
) : LoopInjector<Model, Event, Effect> {

    override fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event> {
        return Mobius.controller(
            Mobius.loop(
                ::update,
                addTaskEffectHandler.provide(uiEffectConsumer)
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
