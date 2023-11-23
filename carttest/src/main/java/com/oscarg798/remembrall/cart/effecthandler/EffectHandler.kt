package com.oscarg798.remembrall.cart.effecthandler

import com.oscarg798.remembrall.cart.Effect
import com.oscarg798.remembrall.cart.Event
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface EffectHandler : suspend (Set<Effect>, (Event) -> Unit) -> Unit

internal class EffectHandlerImpl @Inject constructor(
) : EffectHandler {

    override suspend fun invoke(effects: Set<Effect>, output: (Event) -> Unit) =
        withContext(Dispatchers.IO) {
            effects.forEach {
                //
            }
        }
}