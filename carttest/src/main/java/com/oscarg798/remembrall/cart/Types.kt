package com.oscarg798.remembrall.cart

import androidx.annotation.DrawableRes

internal data class Model(
    val currentText: String = "",
    val options: List<String>? = null,
    val hasPermissions: Boolean = true,
    @DrawableRes val trailingIcon: Int? = null,
) {

    fun isLoaded() = true
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

    data class OnTextChanged(val text: String) : Event
    data class OnFocusChanged(val focused: Boolean) : Event
    data object OnTrailingIconClicked: Event
    data class MutatePermissions(val hasPermissions: Boolean): Event
}

internal sealed interface Effect {

}