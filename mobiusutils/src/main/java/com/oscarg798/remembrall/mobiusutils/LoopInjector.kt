package com.oscarg798.remembrall.mobiusutils

import com.spotify.mobius.Init
import com.spotify.mobius.MobiusLoop

interface LoopInjector<Model, Event, Effect> {

    fun provide(
        initialModel: Model,
        init: Init<Model, Effect>
    ): MobiusLoop.Controller<Model, Event>
}