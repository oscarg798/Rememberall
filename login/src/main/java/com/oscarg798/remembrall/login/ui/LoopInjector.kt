package com.oscarg798.remembrall.login.ui

import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.login.domain.Model
import com.oscarg798.remembrall.login.domain.update
import com.oscarg798.remembrall.login.effecthandler.LoginEffectHandlerProvider
import com.oscarg798.remembrall.login.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.rxutils.SchedulersProvider
import com.spotify.mobius.Init
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.rx3.SchedulerWorkRunner
import javax.inject.Inject

internal class LoopInjectorImpl @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val loginEffectHandler: LoginEffectHandlerProvider,
    private val uiEffectConsumer: UIEffectConsumer,
) : LoopInjector<Model, Event, Effect> {

    override fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event> {
        return Mobius.controller(
            Mobius.loop(
                ::update,
                loginEffectHandler.provide(uiEffectConsumer)
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