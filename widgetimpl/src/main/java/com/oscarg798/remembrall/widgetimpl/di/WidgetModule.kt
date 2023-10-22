package com.oscarg798.remembrall.widgetimpl.di

import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import com.oscarg798.remembrall.widgetimpl.LoopInjectorImpl
import com.oscarg798.remembrall.widgetimpl.RemembrallWidgetUpdaterImpl
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import com.oscarg798.remembrall.widgetimpl.domain.Model
import com.oscarg798.remembrall.widgetimpl.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.widgetimpl.effecthandler.WidgetEffectHandler
import com.oscarg798.remembrall.widgetimpl.usecase.GetSessionState
import com.oscarg798.remembrall.widgetimpl.usecase.GetSessionStateImpl
import com.oscarg798.remembrall.widgetimpl.usecase.GetTaskImpl
import com.oscarg798.remembrall.widgetimpl.usecase.GetTasks
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
@InstallIn(SingletonComponent::class)
internal interface WidgetModule {

    @Binds
    fun bindUIEffectConsumer(impl: UIEffectConsumer): EffectConsumer<Effect>

    @Binds
    fun bindEffectHandler(impl: WidgetEffectHandler): EffectHandlerProvider<Effect, Event>

    @Binds
    fun bindGetSessionState(impl: GetSessionStateImpl): GetSessionState

    @Binds
    fun bindGetTasks(impl: GetTaskImpl): GetTasks

    @Binds
    fun bindLoopInjector(impl: LoopInjectorImpl): LoopInjector<Model, Event, Effect>

    @Binds
    fun bindWidgetUpdated(impl: RemembrallWidgetUpdaterImpl): RemembrallWidgetUpdater

    companion object {

        @Provides
        @Singleton
        fun provideUIEffectState(): MutableSharedFlow<Effect.UIEffect> = MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }
}