package com.oscarg798.remembrall.login.effecthandler

import com.oscarg798.remembrall.login.domain.Effect
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow

internal fun interface UIEffectConsumer : suspend (Effect.UIEffect) -> Unit

internal class UIEffectConsumerImpl @Inject constructor(
    private val uiEffectState: MutableSharedFlow<Effect.UIEffect>
) : UIEffectConsumer {

    override suspend fun invoke(effect: Effect.UIEffect) {
        uiEffectState.emit(effect)
    }
}