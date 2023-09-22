package com.oscarg798.remembrall.login.di

import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.login.domain.Model
import com.oscarg798.remembrall.login.effecthandler.LoginEffectHandlerProvider
import com.oscarg798.remembrall.login.effecthandler.LoginEffectHandlerProviderImpl
import com.oscarg798.remembrall.login.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.login.effecthandler.UIEffectConsumerImpl
import com.oscarg798.remembrall.login.ui.LoopInjectorImpl
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
@InstallIn(ViewModelComponent::class)
internal interface LoginModule {

    @Binds
    fun bindLoginEffectHandlerProvider(
        impl: LoginEffectHandlerProviderImpl
    ): LoginEffectHandlerProvider

    @Binds
    @ViewModelScoped
    fun bindUIEffectConsumer(
        impl: UIEffectConsumerImpl
    ): UIEffectConsumer

    @Binds
    @ViewModelScoped
    fun bindLoginInjector(impl: LoopInjectorImpl): LoopInjector<Model, Event, Effect>

    companion object {

        @Provides
        @ViewModelScoped
        fun provideUIEffectState(): MutableSharedFlow<Effect.UIEffect> = MutableSharedFlow(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    }
}