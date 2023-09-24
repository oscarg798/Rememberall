package com.oscarg798.remembrall.addtask.effecthandler

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow

internal class UIEffectConsumer @Inject constructor(
    private val uiEffectState: MutableSharedFlow<Effect.UIEffect>,
) : EffectConsumer<Effect> {

    override suspend fun invoke(effect: Effect) {
        require(effect is Effect.UIEffect)
        uiEffectState.emit(effect)
    }
}