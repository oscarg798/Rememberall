package com.oscarg798.remembrall.sessionimpl.di

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.sessionimpl.SessionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface SessionBindings {

    @Binds
    @Singleton
    fun bindSession(impl: SessionImpl): Session
}