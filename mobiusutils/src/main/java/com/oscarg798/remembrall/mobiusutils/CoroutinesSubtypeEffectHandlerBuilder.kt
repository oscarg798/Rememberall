package com.oscarg798.remembrall.mobiusutils
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CoroutinesSubtypeEffectHandlerBuilder<F : Any, E : Any> {
    private val job = SupervisorJob()
    private val effectHandlersMap = mutableMapOf<KClass<out F>, suspend (F, (E) -> Unit) -> Unit>()

    inline fun <reified G : F> addAction(
        crossinline action: suspend () -> Unit
    ) = addEffectHandler(G::class) { _, _ ->
        action()
    }

    inline fun <reified G : F> addConsumer(
        crossinline consumer: suspend (G) -> Unit
    ) = addEffectHandler(G::class) { effect, _ ->
        consumer(effect as G)
    }

    inline fun <reified G : F> addFunction(
        crossinline function: suspend (G) -> E
    ): CoroutinesSubtypeEffectHandlerBuilder<F, E> {
        return addEffectHandler(G::class) { effect, output ->
            output(function(effect as G))
        }
    }

    fun addEffectHandler(
        kClass: KClass<out F>,
        function: suspend (F, (E) -> Unit) -> Unit
    ): CoroutinesSubtypeEffectHandlerBuilder<F, E> {
        require(!effectHandlersMap.containsKey(kClass)) {
            "There was is a handler for the Effect ${kClass.simpleName}"
        }
        effectHandlersMap[kClass] = function
        return this
    }

    fun build(coroutineContext: CoroutineContext) = build(CoroutineScope(coroutineContext + job))

    private fun build(scope: CoroutineScope) = Connectable { eventConsumer ->
        object : Connection<F> {
            override fun accept(effect: F) {
                val effectHandler = effectHandlersMap[effect::class]
                    ?: error("No effectHandler for $effect")
                scope.launch {
                    effectHandler(effect) { event ->
                        eventConsumer.accept(event)
                    }
                }
            }

            override fun dispose() {
                job.cancel()
            }
        }
    }
}