package com.oscarg798.remembrall.detail.di

import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.detail.domain.Model
import com.oscarg798.remembrall.detail.effecthandler.TaskDetailEffectHandler
import com.oscarg798.remembrall.detail.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.detail.navigation.TaskDetailDeepLinkRouteFactory
import com.oscarg798.remembrall.detail.ui.TaskDetailLoopInjector
import com.oscarg798.remembrall.detail.ui.TaskDetailPage
import com.oscarg798.remembrall.detail.ui.TaskDetailViewModel
import com.oscarg798.remembrall.detail.usecase.GetTask
import com.oscarg798.remembrall.detail.usecase.GetTaskImpl
import com.oscarg798.remembrall.detail.usecase.MarkTaskAsCompleted
import com.oscarg798.remembrall.detail.usecase.MarkTaskAsCompletedImpl
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.navigationutils.DeeplinkRouteFactoryKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface TaskDetailModule {

    @Binds
    fun bindLoopInjector(impl: TaskDetailLoopInjector): LoopInjector<Model, Event, Effect>

    @Binds
    fun bindEffectHandler(impl: TaskDetailEffectHandler): EffectHandlerProvider<Effect, Event>

    @Binds
    fun bindGetTask(impl: GetTaskImpl): GetTask

    @Binds
    fun bindMarkAsCompleted(impl: MarkTaskAsCompletedImpl): MarkTaskAsCompleted

    @Binds
    fun bindUIEffectConsumer(impl: UIEffectConsumer): EffectConsumer<Effect>

    @Binds
    @IntoMap
    @DeeplinkRouteFactoryKey(Route.DETAIL)
    fun bindTaskDetailDeepLinkRouteFactory(
        impl: TaskDetailDeepLinkRouteFactory
    ): DeeplinkRouteFactory

    companion object {

        @Provides
        @ActivityRetainedScoped
        fun provideUiEffectState(): MutableSharedFlow<Effect.UIEffect> {
            return MutableSharedFlow(
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        }
    }
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface TaskDetailEntryPoint {

    fun viewModelFactory(): TaskDetailViewModel.Factory
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object TaskDetailPageProvider {

    @IntoSet
    @Provides
    fun provide(): Page = TaskDetailPage
}