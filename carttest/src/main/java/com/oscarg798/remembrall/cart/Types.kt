package com.oscarg798.remembrall.cart

internal data class Model(
    val store: Store? = null,
    val promotion: List<Promotion>? = null,
    val cart: Map<String, CartProduct>? = null,
    val decoratedCart: Map<String, Product>? = null,
    val recommendedProducts: List<Product>? = null,
) {

    fun isLoaded() = store != null && decoratedCart != null && cart != null
}

internal data class CartProduct(
    val productId: String,
    val quantity: Int
)

internal data class Store(
    val name: String,
    val country: String
)

internal data class Product(
    val id: String,
    val name: String,
    val price: Float
)

internal sealed interface Promotion {

    data class Product(val productId: String, val discount: Float) : Promotion
    data class Delivery(val price: Float) : Promotion
}

internal sealed interface Event {

    data class OnStoreFound(val store: Store) : Event
    data class OnCartFound(val cart: Map<String, CartProduct>) : Event
    data class OnCartDecorated(val decoration: Map<String, Product>) : Event
    data class OnRecommendedProductsFound(val recommendations: List<Product>) : Event
    data class OnPromotionsFound(val promotions: List<Promotion>): Event
}

internal sealed interface Effect {
    data object GetCart : Effect
    data object GetStore : Effect
    data class DecorateCart(val productsInCart: Set<String>) : Effect
    data object GetRecommendedProducts : Effect
    data object GetPromotions: Effect
}