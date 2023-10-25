package com.oscarg798.remembrall.cart.di

import com.oscarg798.remembrall.cart.ui.CartPage
import com.oscarg798.remembrall.cart.usecase.DecorateCart
import com.oscarg798.remembrall.cart.usecase.DecorateCartImpl
import com.oscarg798.remembrall.cart.usecase.GetCart
import com.oscarg798.remembrall.cart.usecase.GetCartImpl
import com.oscarg798.remembrall.cart.usecase.GetPromotions
import com.oscarg798.remembrall.cart.usecase.GetPromotionsImpl
import com.oscarg798.remembrall.cart.usecase.GetRecommendedProducts
import com.oscarg798.remembrall.cart.usecase.GetRecommendedProductsImpl
import com.oscarg798.remembrall.cart.usecase.GetStore
import com.oscarg798.remembrall.cart.usecase.GetStoreImpl
import com.oscarg798.remembrall.navigation.Page
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
internal interface CartModule {

    @Binds
    fun bindDecorateCart(impl: DecorateCartImpl): DecorateCart

    @Binds
    fun bindGetCart(impl: GetCartImpl): GetCart

    @Binds
    fun bindGetPromotions(impl: GetPromotionsImpl): GetPromotions

    @Binds
    fun bindGetRecommendedProducts(impl: GetRecommendedProductsImpl): GetRecommendedProducts

    @Binds
    fun bindGetStore(impl: GetStoreImpl): GetStore
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface PageModule {

    @Binds
    @IntoSet
    fun bindPage(impl: CartPage): Page
}