package com.oscarg798.remembrall.cart.usecase

import com.oscarg798.remembrall.cart.Promotion
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.delay

internal interface GetPromotions : suspend () -> List<Promotion>

internal class GetPromotionsImpl @Inject constructor() : GetPromotions {

    override suspend fun invoke(): List<Promotion> {
        delay(500)

        return listOf(Promotion.Delivery(Random.nextFloat()))
    }
}