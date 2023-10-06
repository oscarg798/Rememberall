package com.oscarg798.remembrall.navigationimpl.di

import com.oscarg798.remembrall.navigation.DeepLinkUriBuilder
import com.oscarg798.remembrall.navigationimpl.DeepLinkUriBuilderImpl
import com.oscarg798.remembrall.navigationimpl.NavigatorFactory
import com.oscarg798.remembrall.navigationimpl.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface NavigationModule {

    @Binds
    fun bindNavigatorFactory(impl: NavigatorImpl.Factory): NavigatorFactory

    @Binds
    fun bindDeepLinkUriBuilder(impl: DeepLinkUriBuilderImpl): DeepLinkUriBuilder
}