package com.oscarg798.remembrall.home.di

import com.oscarg798.remembrall.home.HomePage
import com.oscarg798.remembrall.navigation.Page
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface HomePageProvider  {

    @Binds
    @IntoSet
    fun bindPage(impl: HomePage): Page
}