package com.oscarg798.remembrall.cart.usecase

import com.oscarg798.remembrall.cart.CartProduct
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.delay

internal interface GetCart : suspend () -> Map<String, CartProduct>

internal class GetCartImpl @Inject constructor() : GetCart {

    override suspend fun invoke(): Map<String, CartProduct> {
        delay(700)
        return (0 until 10).map { UUID.randomUUID().toString() }.associateWith {
            CartProduct(it, Random.nextInt(1, 5))
        }
    }
}