package com.oscarg798.remembrall.activityproviderimpl.di

import android.app.Application
import com.oscarg798.remembrall.activityprovider.ActivityProvider
import com.oscarg798.remembrall.activityproviderimpl.ActivityProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ActivityProviderBindings {

    @Binds
    @Singleton
    fun bindActivityProvider(impl: ActivityProviderImpl): ActivityProvider

    @Binds
    @Singleton
    fun bindActivityLifecycleCallback(
        impl: ActivityProvider
    ): Application.ActivityLifecycleCallbacks
}
