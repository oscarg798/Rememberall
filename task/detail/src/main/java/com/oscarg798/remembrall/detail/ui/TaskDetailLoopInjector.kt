package com.oscarg798.remembrall.detail.ui

import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.detail.domain.Model
import com.oscarg798.remembrall.detail.domain.update
import com.oscarg798.remembrall.detail.effecthandler.TaskDetailEffectHandler
import com.oscarg798.remembrall.detail.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.rxutils.SchedulersProvider
import com.spotify.mobius.Connectable
import com.spotify.mobius.Init
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.rx3.SchedulerWorkRunner
import javax.inject.Inject

internal class TaskDetailLoopInjector @Inject constructor(
    private val uiEffectConsumer: UIEffectConsumer,
    private val schedulersProvider: SchedulersProvider,
    private val taskDetailEffectHandler: TaskDetailEffectHandler,
): LoopInjector<Model, Event, Effect>{

    override fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event> {
        return Mobius.controller(
            Mobius.loop(
                ::update,
                taskDetailEffectHandler.provide(uiEffectConsumer)
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