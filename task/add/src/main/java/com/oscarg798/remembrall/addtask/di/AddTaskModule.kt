package com.oscarg798.remembrall.addtask.di

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Model
import com.oscarg798.remembrall.addtask.effecthandler.AddTaskEffectHandler
import com.oscarg798.remembrall.addtask.effecthandler.UIEffectConsumer
import com.oscarg798.remembrall.addtask.ui.AddTaskPage
import com.oscarg798.remembrall.addtask.ui.AddTaskViewModel
import com.oscarg798.remembrall.addtask.ui.LoopInjectorImpl
import com.oscarg798.remembrall.addtask.usecase.AddTask
import com.oscarg798.remembrall.addtask.usecase.AddTaskImpl
import com.oscarg798.remembrall.addtask.usecase.CurrentDateProvider
import com.oscarg798.remembrall.addtask.usecase.CurrentDateProviderImpl
import com.oscarg798.remembrall.addtask.usecase.FieldValidator
import com.oscarg798.remembrall.addtask.usecase.FieldValidatorImpl
import com.oscarg798.remembrall.addtask.usecase.FormatDueDate
import com.oscarg798.remembrall.addtask.usecase.FormatDueDateImpl
import com.oscarg798.remembrall.addtask.usecase.GetAvailableTaskPriorities
import com.oscarg798.remembrall.addtask.usecase.GetAvailableTaskPrioritiesImpl
import com.oscarg798.remembrall.addtask.usecase.GetDueDatePickerInitialDate
import com.oscarg798.remembrall.addtask.usecase.GetDueDatePickerInitialDateImpl
import com.oscarg798.remembrall.addtask.usecase.GetTask
import com.oscarg798.remembrall.addtask.usecase.GetTaskImpl
import com.oscarg798.remembrall.addtask.usecase.UpdateCalendarInformation
import com.oscarg798.remembrall.addtask.usecase.UpdateCalendarInformationImpl
import com.oscarg798.remembrall.addtask.usecase.UpdateTask
import com.oscarg798.remembrall.addtask.usecase.UpdateTaskImpl
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.navigation.Page
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.IntoSet
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface AddTaskModule {

    @Binds
    fun bindAddTaskEffectHandler(impl: AddTaskEffectHandler): EffectHandlerProvider<Effect, Event>

    @Binds
    @ActivityRetainedScoped
    fun bindLoopInjector(impl: LoopInjectorImpl): LoopInjector<Model, Event, Effect>

    @Binds
    @ActivityRetainedScoped
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

    @Binds
    fun bindAddTaskUseCase(impl: AddTaskImpl): AddTask

    @Binds
    fun bindGetTask(impl: GetTaskImpl): GetTask

    @Binds
    fun bindUpdateTask(impl: UpdateTaskImpl): UpdateTask

    @Binds
    fun bindFieldValidator(impl: FieldValidatorImpl): FieldValidator

    @Binds
    fun bindUpdateCalendarInformation(impl: UpdateCalendarInformationImpl): UpdateCalendarInformation

    companion object {

        @Provides
        @ActivityRetainedScoped
        fun provideUiEffectState(): MutableSharedFlow<Effect.UIEffect> {
            return MutableSharedFlow(
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        }

        @IntoSet
        @Provides
        fun provideAddTaskPage(): Page = AddTaskPage
    }
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface AddTaskEntryPoint {

    fun addTaskViewModelFactory(): AddTaskViewModel.Factory
}
