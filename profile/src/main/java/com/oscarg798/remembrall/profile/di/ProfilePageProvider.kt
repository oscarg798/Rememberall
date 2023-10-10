package com.oscarg798.remembrall.profile.di

import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.profile.ui.ProfilePage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object ProfilePageProvider {

    @IntoSet
    @Provides
    fun providePage(): Page = ProfilePage
}