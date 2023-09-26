package com.oscarg798.remembrall.login.effecthandler

import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Effect.UIEffect
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow

internal fun interface UIEffectConsumer : EffectConsumer<Effect>

internal class UIEffectConsumerImpl @Inject constructor(
    private val uiEffectState: MutableSharedFlow<Effect.UIEffect>
) : UIEffectConsumer {

    override suspend fun invoke(effect: Effect) {
        require(effect is UIEffect)
        uiEffectState.emit(effect)
    }
}
