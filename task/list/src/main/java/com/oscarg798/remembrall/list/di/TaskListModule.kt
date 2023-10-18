package com.oscarg798.remembrall.list.di

import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.domain.model.Model
import com.oscarg798.remembrall.list.effecthandler.TaskListEffectHandler
import com.oscarg798.remembrall.list.ui.LoopInjectorImpl
import com.oscarg798.remembrall.list.usecase.GetInitialIndexPosition
import com.oscarg798.remembrall.list.usecase.GetInitialIndexPositionImpl
import com.oscarg798.remembrall.list.usecase.GetTaskGrouped
import com.oscarg798.remembrall.list.usecase.GetTaskGroupedImpl
import com.oscarg798.remembrall.list.usecase.GetTaskOptions
import com.oscarg798.remembrall.list.usecase.GetTaskOptionsImpl
import com.oscarg798.remembrall.list.usecase.GetTasksUseCase
import com.oscarg798.remembrall.list.usecase.GetTasksUseCaseImpl
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
@InstallIn(ViewModelComponent::class)
internal interface TaskListModule {

    @Binds
    fun bindLoopInjector(impl: LoopInjectorImpl): LoopInjector<Model, Event, Effect>

    @Binds
    fun bindEffectHandler(impl: TaskListEffectHandler): EffectHandlerProvider<Effect, Event>

    @Binds
    fun bindGetInitialIndexPosition(impl: GetInitialIndexPositionImpl): GetInitialIndexPosition

    @Binds
    fun bindGetTaskGrouped(impl: GetTaskGroupedImpl): GetTaskGrouped

    @Binds
    fun bindGetTasks(impl: GetTasksUseCaseImpl): GetTasksUseCase

    @Binds
    fun bindGetTaskOptions(impl: GetTaskOptionsImpl): GetTaskOptions

    companion object {

        @Provides
        @ViewModelScoped
        fun provideUiEffectState(): MutableSharedFlow<Effect.UIEffect> {
            return MutableSharedFlow(
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        }
    }
}