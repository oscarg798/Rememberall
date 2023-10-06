package com.oscarg798.remembrall.task.addrouteprovider.di

import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigationutils.DeeplinkRouteFactoryKey
import com.oscarg798.remembrall.task.addroute.AddRoute
import com.oscarg798.remembrall.task.addrouteprovider.AddDeeplinkRouteFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
internal interface AddRouteModule {

    @Binds
    @IntoMap
    @DeeplinkRouteFactoryKey(AddRoute::class)
    fun bindDeeplinkRouteFactory(impl: AddDeeplinkRouteFactory): DeeplinkRouteFactory
}