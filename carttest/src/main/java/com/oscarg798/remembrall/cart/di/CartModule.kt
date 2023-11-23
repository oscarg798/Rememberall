package com.oscarg798.remembrall.cart.di

import com.oscarg798.remembrall.cart.effecthandler.EffectHandler
import com.oscarg798.remembrall.cart.effecthandler.EffectHandlerImpl
import com.oscarg798.remembrall.cart.ui.CartPage
import com.oscarg798.remembrall.cart.ui.PermissionChecker
import com.oscarg798.remembrall.cart.ui.PermissionCheckerImpl
import com.oscarg798.remembrall.cart.ui.TextComponentDecorator
import com.oscarg798.remembrall.cart.ui.TextComponentDecoratorImpl
import com.oscarg798.remembrall.navigation.Page
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
internal interface CartModule {

    @Binds
    fun bindEffectHandler(impl: EffectHandlerImpl): EffectHandler

    @Binds
    fun bindTextComponentDecorator(impl: TextComponentDecoratorImpl): TextComponentDecorator

    @Binds
    @ViewModelScoped
    fun bindPermissionChecker(impl: PermissionCheckerImpl): PermissionChecker
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface PageModule {

    @Binds
    @IntoSet
    fun bindPage(impl: CartPage): Page
}