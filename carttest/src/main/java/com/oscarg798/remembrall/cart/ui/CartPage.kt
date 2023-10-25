package com.oscarg798.remembrall.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.cart.CartViewModel
import com.oscarg798.remembrall.cart.Promotion
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import javax.inject.Inject

internal class CartPage @Inject constructor() : Page {
    override fun build(builder: NavGraphBuilder) {
        builder.CartScreen()
    }
}

private fun NavGraphBuilder.CartScreen() = composable(
    Route.CART.path, deepLinks = listOf(
        navDeepLink {
            uriPattern = Route.LOGIN.uriPattern.toString()
        })
) {
    val viewModel: CartViewModel = hiltViewModel(it)
    val state by viewModel.model.collectAsState()


    RemembrallScaffold {
        RemembrallPage(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.Small)
            ) {
                if (state.isLoaded()) {
                    item("title") {
                        Text(
                            text = state.store!!.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    items(state.decoratedCart!!.values.toList(), {
                        it.id
                    }) { product ->
                        Column(Modifier.fillMaxSize()) {
                            Text(text = product.name)
                            Text(text = "$${product.price}")
                        }
                    }

                    items(state.promotion!!) {
                        when (it) {
                            is Promotion.Delivery -> Text(text = "Discount in delivery ${it.price}")
                            is Promotion.Product -> Text(text = "Discount in product ${it.productId}")
                        }
                    }
                } else {
                    item("Loading") {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}