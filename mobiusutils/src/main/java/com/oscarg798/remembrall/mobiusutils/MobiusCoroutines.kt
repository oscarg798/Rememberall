package com.oscarg798.remembrall.mobiusutils

interface MobiusCoroutines {

    companion object {
        fun <F : Any, E : Any> subtypeEffectHandler() =
            CoroutinesSubtypeEffectHandlerBuilder<F, E>()
    }
}