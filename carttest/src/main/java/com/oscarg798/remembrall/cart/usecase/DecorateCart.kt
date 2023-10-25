package com.oscarg798.remembrall.cart.usecase

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.oscarg798.remembrall.cart.Product
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.delay

internal interface DecorateCart : suspend (Set<String>) -> Map<String, Product>

internal class DecorateCartImpl @Inject constructor() : DecorateCart {

    override suspend fun invoke(productIds: Set<String>): Map<String, Product> {
        delay(5_00)

        return productIds.associateWith {
            Product(
                id = it,
                name = LoremIpsum(Random.nextInt(1, 3)).values.joinToString(""),
                price = Random.nextFloat()
            )
        }
    }
}