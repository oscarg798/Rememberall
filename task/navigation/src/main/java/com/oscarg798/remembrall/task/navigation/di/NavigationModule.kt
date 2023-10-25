package com.oscarg798.remembrall.task.navigation.di

import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.navigationutils.DeeplinkRouteFactoryKey
import com.oscarg798.remembrall.task.navigation.TaskDetailDeepLinkRouteFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface NavigationModule {

    @Binds
    @IntoMap
    @DeeplinkRouteFactoryKey(Route.DETAIL)
    fun bindTaskDetailDeepLinkRouteFactory(
        impl: TaskDetailDeepLinkRouteFactory
    ): DeeplinkRouteFactory
}