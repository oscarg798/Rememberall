package com.oscarg798.remembrall.splash.di

import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.splash.effecthandler.SplashEffectHandler
import com.oscarg798.remembrall.splash.domain.Effect
import com.oscarg798.remembrall.splash.domain.Event
import com.oscarg798.remembrall.splash.domain.Model
import com.oscarg798.remembrall.splash.ui.LoopInjectorImpl
import com.oscarg798.remembrall.splash.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.splash.usecase.IsUserLoggedIn
import com.oscarg798.remembrall.splash.usecase.IsUserLoggedInImpl
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
internal interface SplashModule {

    @Binds
    fun bindIsUserLoggedIn(impl: IsUserLoggedInImpl): IsUserLoggedIn

    @Binds
    @ViewModelScoped
    fun bindSplashEffectHandler(impl: SplashEffectHandler): EffectHandlerProvider<Effect, Event>

    @Binds
    @ViewModelScoped
    fun bindLoopInjector(impl: LoopInjectorImpl): LoopInjector<Model, Event, Effect>

    @Binds
    @ViewModelScoped
    fun bindUIEffectConsumer(impl: UIEffectConsumer): EffectConsumer<Effect>

    companion object {

        @Provides
        @ViewModelScoped
        fun provideUIEffectState(): MutableSharedFlow<Effect.UIEffect> = MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }
}