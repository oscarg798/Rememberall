package com.oscarg798.remembrall.addtask.effecthandler

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.usecase.AddTaskUseCase
import com.oscarg798.remembrall.addtask.usecase.FormatDueDate
import com.oscarg798.remembrall.addtask.usecase.GetAvailableTaskPriorities
import com.oscarg798.remembrall.addtask.usecase.GetDueDatePickerInitialDate
import com.oscarg798.remembrall.addtask.usecase.GetTask
import com.oscarg798.remembrall.addtask.usecase.UpdateTask
import com.oscarg798.remembrall.mobiusutils.EffectConsumer
import com.oscarg798.remembrall.mobiusutils.EffectHandlerProvider
import com.oscarg798.remembrall.mobiusutils.MobiusCoroutines
import com.spotify.mobius.Connectable
import javax.inject.Inject

internal class AddTaskEffectHandler @Inject constructor(
    private val getTask: GetTask,
    private val updateTask: UpdateTask,
    private val formatDueDate: FormatDueDate,
    private val addTaskUseCase: AddTaskUseCase,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val getAvailableTaskPriorities: GetAvailableTaskPriorities,
    private val getDueDatePickerInitialDate: GetDueDatePickerInitialDate,
) : EffectHandlerProvider<Effect, Event> {

    override fun provide(uiEffectConsumer: EffectConsumer<Effect>): Connectable<Effect, Event> {
        return MobiusCoroutines.subtypeEffectHandler<Effect, Event>()
            .addFunction(getTask)
            .addFunction(updateTask)
            .addFunction(formatDueDate)
            .addFunction(addTaskUseCase)
            .addFunction(getAvailableTaskPriorities)
            .addFunction(getDueDatePickerInitialDate)
            .addConsumer<Effect.UIEffect.Close>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.ShowError>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.ShowPriorityPicker>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.ShowAttendeesPicker>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.DismissDueDatePicker>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.ShowDueDateDatePicker>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.DismissAttendeesPicker>(uiEffectConsumer)
            .addConsumer<Effect.UIEffect.DismissTaskPriorityPicker>(uiEffectConsumer)
            .build(coroutineContextProvider.computation)
    }
}
