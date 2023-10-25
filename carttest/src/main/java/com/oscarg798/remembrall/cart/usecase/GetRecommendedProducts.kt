package com.oscarg798.remembrall.cart.usecase

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.oscarg798.remembrall.cart.Product
import java.util.UUID
import kotlin.random.Random

internal interface GetRecommendedProducts : suspend () -> List<Product>

internal class GetRecommendedProductsImpl : GetRecommendedProducts {

    override suspend fun invoke(): List<Product> {
        return (0 until 3).map {
            Product(
                id = UUID.randomUUID().toString(),
                name = LoremIpsum(Random.nextInt(1, 3)).values.joinToString(""),
                price = Random.nextFloat()
            )
        }
    }
}