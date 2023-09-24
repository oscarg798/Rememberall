package com.oscarg798.remembrall.addtask.di

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Model
import com.oscarg798.remembrall.addtask.effecthandler.AddTaskEffectHandler
import com.oscarg798.remembrall.addtask.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.addtask.ui.LoopInjectorImpl
import com.oscarg798.remembrall.addtask.usecase.CurrentDateProvider
import com.oscarg798.remembrall.addtask.usecase.CurrentDateProviderImpl
import com.oscarg798.remembrall.addtask.usecase.FormatDueDate
import com.oscarg798.remembrall.addtask.usecase.FormatDueDateImpl
import com.oscarg798.remembrall.addtask.usecase.GetAvailableTaskPriorities
import com.oscarg798.remembrall.addtask.usecase.GetAvailableTaskPrioritiesImpl
import com.oscarg798.remembrall.addtask.usecase.GetDueDatePickerInitialDate
import com.oscarg798.remembrall.addtask.usecase.GetDueDatePickerInitialDateImpl
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
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
internal interface AddTaskModule {

    @Binds
    fun bindAddTaskEffectHandler(impl: AddTaskEffectHandler): EffectHandlerProvider<Effect, Event>

    @Binds
    @ViewModelScoped
    fun bindLoopInjector(impl: LoopInjectorImpl): LoopInjector<Model, Event, Effect>

    @Binds
    @ViewModelScoped
    fun bindUiEffectConsumer(impl: UIEffectConsumer): EffectConsumer<Effect>

    @Binds
    fun bindGetAvailablePriorities(impl: GetAvailableTaskPrioritiesImpl): GetAvailableTaskPriorities

    @Binds
    fun bindGetDueDatePickerInitialDate(
        impl: GetDueDatePickerInitialDateImpl
    ): GetDueDatePickerInitialDate

    @Binds
    fun bindFormatDueDate(impl: FormatDueDateImpl): FormatDueDate

    @Binds
    fun bindCurrentDateProvider(impl: CurrentDateProviderImpl): CurrentDateProvider

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