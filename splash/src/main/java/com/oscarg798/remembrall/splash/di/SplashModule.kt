package com.oscarg798.remembrall.splash.di

import com.oscarg798.remembrall.splash.usecase.IsUserLoggedIn
import com.oscarg798.remembrall.splash.usecase.IsUserLoggedInImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface SplashModule {

    @Binds
    fun bindIsUserLoggedIn(impl: IsUserLoggedInImpl): IsUserLoggedIn
}