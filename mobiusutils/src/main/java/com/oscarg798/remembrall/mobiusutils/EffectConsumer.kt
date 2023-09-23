package com.oscarg798.remembrall.mobiusutils

fun interface EffectConsumer<Effect> : suspend (Effect) -> Unit