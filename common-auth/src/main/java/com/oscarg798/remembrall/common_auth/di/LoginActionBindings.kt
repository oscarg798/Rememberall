package com.oscarg798.remembrall.common_auth.di

import com.oscarg798.remembrall.auth.LoginAction
import com.oscarg798.remembrall.common_auth.network.restclient.FinishLoginAction
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface LoginActionBindings {

    @Binds
    @IntoSet
    fun bindLoginAction(impl: FinishLoginAction): LoginAction
}