package com.oscarg798.remembrall.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.cart.Upcoming.Next
import com.oscarg798.remembrall.cart.usecase.DecorateCart
import com.oscarg798.remembrall.cart.usecase.GetCart
import com.oscarg798.remembrall.cart.usecase.GetPromotions
import com.oscarg798.remembrall.cart.usecase.GetRecommendedProducts
import com.oscarg798.remembrall.cart.usecase.GetStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
internal class CartViewModel @Inject constructor(
    private val getCart: GetCart,
    private val getStore: GetStore,
    private val decorateCart: DecorateCart,
    private val getPromotions: GetPromotions,
    private val getRecommendedProducts: GetRecommendedProducts,
) : ViewModel() {

    private val _model = MutableStateFlow(Model())
    private val effects =
        MutableSharedFlow<Effect>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    private val loop = MutableStateFlow<Upcoming>(Upcoming.Effects(setOf(Effect.GetStore)))
    private val events =
        MutableSharedFlow<Event>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    val model = _model.asStateFlow()

    init {
        viewModelScope.launch {
            loop.collectLatest {
                when (it) {
                    is Upcoming.Effects -> dispatchEffects(it.effects)
                    is Next -> {
                        _model.value = it.model
                        dispatchEffects(it.effects)

                    }

                    is Upcoming.NoChange -> Unit
                }
            }
        }

        viewModelScope.launch {
            events.collectLatest {
                loop.emit(update(_model.value, it))
            }
        }

        viewModelScope.launch {
            effects.collectLatest {
                //Each usecase must do this but, meh
                async(Dispatchers.IO) {
                    when (it) {
                        is Effect.DecorateCart -> decorateCart(it.productsInCart)
                        Effect.GetCart -> getCart()
                        Effect.GetStore -> getStore()
                        Effect.GetPromotions -> getPromotions()
                        Effect.GetRecommendedProducts -> getRecommendedProducts()
                    }
                }
            }
        }

        //First effect
        viewModelScope.launch { effects.emit(Effect.GetStore) }
    }

    private suspend fun dispatchEffects(effects: Set<Effect>) {
        effects.onEach { effect -> this.effects.emit(effect) }
    }

    private suspend fun update(
        _model: Model,
        event: Event
    ): Upcoming = withContext(Dispatchers.IO) {
        when (event) {
            is Event.OnCartDecorated -> Next(_model.copy(decoratedCart = event.decoration))
            is Event.OnCartFound -> Next(
                _model.copy(cart = event.cart),
                setOf(Effect.DecorateCart(event.cart.keys))
            )

            is Event.OnStoreFound -> Next(
                _model.copy(store = event.store),
                setOf(Effect.GetCart)
            )

            is Event.OnPromotionsFound -> Next(
                _model.copy(promotion = event.promotions)
            )

            is Event.OnRecommendedProductsFound -> Next(
                _model.copy(recommendedProducts = event.recommendations)
            )
        }
    }

}