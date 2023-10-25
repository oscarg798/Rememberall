package com.oscarg798.remembrall.widgetimpl.di

import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigation.Navigator
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.navigationutils.DeeplinkRouteFactoryKey
import com.oscarg798.remembrall.task.navigation.TaskDetailDeepLinkRouteFactory
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import com.oscarg798.remembrall.widgetimpl.LoopInjectorImpl
import com.oscarg798.remembrall.widgetimpl.RemembrallWidgetUpdaterImpl
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import com.oscarg798.remembrall.widgetimpl.domain.Model
import com.oscarg798.remembrall.widgetimpl.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.widgetimpl.effecthandler.WidgetEffectHandler
import com.oscarg798.remembrall.widgetimpl.navigation.WidgetNavigator
import com.oscarg798.remembrall.widgetimpl.navigation.WidgetNavigatorQualifier
import com.oscarg798.remembrall.widgetimpl.usecase.LocalDateTimeProvider
import com.oscarg798.remembrall.widgetimpl.usecase.CalendarProviderImpl
import com.oscarg798.remembrall.widgetimpl.usecase.ForceWidgetRefresh
import com.oscarg798.remembrall.widgetimpl.usecase.ForceWidgetRefreshImpl
import com.oscarg798.remembrall.widgetimpl.usecase.GetSessionState
import com.oscarg798.remembrall.widgetimpl.usecase.GetSessionStateImpl
import com.oscarg798.remembrall.widgetimpl.usecase.GetTaskImpl
import com.oscarg798.remembrall.widgetimpl.usecase.GetTaskWindow
import com.oscarg798.remembrall.widgetimpl.usecase.GetTaskWindowImpl
import com.oscarg798.remembrall.widgetimpl.usecase.GetTasks
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
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

    @Binds
    fun bindCalendarProvider(impl: CalendarProviderImpl): LocalDateTimeProvider

    @Binds
    fun bindGetTaskWindow(impl: GetTaskWindowImpl): GetTaskWindow

    @Binds
    fun bindRefreshWidgetRefresh(impl: ForceWidgetRefreshImpl): ForceWidgetRefresh

    @Binds
    @WidgetNavigatorQualifier
    fun bindWidgetNavigator(impl: WidgetNavigator): Navigator

    companion object {

        @Provides
        @Singleton
        fun provideUIEffectState(): MutableSharedFlow<Effect.UIEffect> = MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }
}