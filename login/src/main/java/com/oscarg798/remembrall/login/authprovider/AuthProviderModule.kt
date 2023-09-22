package com.oscarg798.remembrall.login.authprovider

import com.oscarg798.remembrall.auth.ExternalAuthProvider
import com.oscarg798.remembrall.auth.LoginAction
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
internal interface AuthProviderModule {

    @Binds
    @ViewModelScoped
    fun bindExternalAuthProvider(impl: OneTapAuthProvider): ExternalAuthProvider
}

@Module
@InstallIn(SingletonComponent::class)
internal interface LoginActionBindings {

    @Binds
    @IntoSet
    fun bindRequestScopesLoginAction(impl: RequestScopesLoginAction): LoginAction
}