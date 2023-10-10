package com.oscarg798.remembrall.list.di

import com.oscarg798.remembrall.homeutils.HomeContent
import com.oscarg798.remembrall.list.ui.TaskListHomeContent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object TaskListHomeContentProvider {

    @Provides
    fun provideTaskListHomeContent(): HomeContent = TaskListHomeContent
}