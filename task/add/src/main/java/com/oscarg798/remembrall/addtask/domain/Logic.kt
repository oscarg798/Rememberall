package com.oscarg798.remembrall.addtask.domain

import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Next.noChange

internal typealias Upcoming = Next<Model, Effect>

internal fun update(
    model: Model,
    event: Event
): Upcoming = when (event) {
    Event.DismissAttendeePicker -> onDismissAttendeePicker()
    Event.OnAttendeeActionClicked -> onAttendeesActionClicked()
    Event.OnBackClicked -> onBackClicked()
    Event.OnCalendarActionClicked -> onCalendarActionClicked(model)
    Event.OnCalendarActionLongClicked -> onCalendarActionLongClicked(model)
    Event.OnCloseClicked -> onCloseClicked()
    Event.OnSaveActionClicked -> onSaveActionClicked(model)
    Event.OnTagActionClicked -> onTagActionClicked(model)
    Event.OnTagActionLongClicked -> onTagActionLongClicked(model)
    Event.OnTaskPrioritySelectorDismissed -> onTaskPrioritySelectorDismissed()
    Event.OnTaskSaved -> onBackClicked()
    is Event.OnAttendeeAdded -> onAttendeeAdded(model, event)
    is Event.OnAttendeeRemoved -> onAttendeeRemoved(model, event)
    is Event.OnDescriptionChanged -> onDescriptionChange(model, event)
    is Event.OnDueDateDateAndTimeSelected -> onDueDateDateAndTimeSelected(model, event)
    is Event.OnDueDateFormatted -> onDueDateFormatted(model, event)
    is Event.OnDueDatePickerInitialDateFound -> onDueDatePickerInitialDateFound(event)
    is Event.OnError -> onValidationError(model, event)
    is Event.OnPriorityChanged -> onPriorityChanged(model, event)
    is Event.OnTaskLoaded -> onTaskLoaded(model, event)
    is Event.OnTaskPrioritiesFound -> onTaskPrioritiesFound(model, event)
    is Event.OnTitleChanged -> onTitleChanged(model, event)
}

private fun onTaskLoaded(model: Model, event: Event.OnTaskLoaded): Upcoming {
    return if (model.editableTask == event.task) {
        noChange()
    } else {
        val task = event.task
        return next(
            model.copy(
                editableTask = task,
                title = event.task.title,
                description = task.description.orEmpty(),
                dueDate = event.dueDate,
                priority = task.priority,
                attendees = task.calendarSyncInformation?.attendees?.map { it.email }?.toSet()
                    ?: emptySet(),
            )
        )
    }
}

private fun onTagActionLongClicked(model: Model): Upcoming {
    return if (model.priority == null) {
        noChange()
    } else {
        next(model.copy(priority = null))
    }
}

private fun onCalendarActionLongClicked(model: Model): Upcoming {
    return if (model.dueDate == null) {
        noChange()
    } else {
        next(model.copy(dueDate = null))
    }
}

private fun onValidationError(model: Model, event: Event.OnError): Upcoming {
    val effects = setOf(
        when (event.error) {
            Error.AddingTask -> Effect.UIEffect.ShowError(
                Effect.UIEffect.ShowError.Error.ErrorAddingTask
            )

            Error.Auth -> Effect.UIEffect.NavigateToLogin
            Error.InvalidAttendeesFormat ->
                Effect.UIEffect.ShowError(Effect.UIEffect.ShowError.Error.InvalidAttendeesFormat)

            Error.InvalidName ->
                Effect.UIEffect.ShowError(Effect.UIEffect.ShowError.Error.InvalidName)

            Error.CanNotRemoveDueDateWhileUpdating -> Effect.UIEffect.ShowError(
                Effect.UIEffect.ShowError.Error.CanNotRemoveDueDateWhileUpdating
            )
        }
    )

    return if (model.loading) {
        next(model.copy(loading = false), effects)
    } else {
        dispatch(effects)
    }
}

private fun onSaveActionClicked(model: Model): Upcoming {
    return if (model.loading || !model.isLoaded()) {
        noChange()
    } else if (!model.isEditMode()) {
        next(
            model.copy(loading = true),
            setOf(
                Effect.SaveTask(
                    title = model.title,
                    description = model.description,
                    dueDate = model.dueDate,
                    priority = model.priority,
                    attendees = model.attendees
                )
            )
        )
    } else {
        next(
            model.copy(loading = true),
            setOf(
                Effect.UpdateTask(
                    title = model.title,
                    description = model.description,
                    dueDate = model.dueDate,
                    priority = model.priority,
                    attendees = model.attendees,
                    originalTask = model.editableTask!!
                )
            )
        )
    }
}

private fun onBackClicked(): Upcoming = dispatch(setOf(Effect.UIEffect.Close))

private fun onCloseClicked(): Upcoming = dispatch(setOf(Effect.UIEffect.Close))

private fun onTitleChanged(model: Model, event: Event.OnTitleChanged): Upcoming =
    if (model.title == event.title) {
        noChange()
    } else {
        next(model.copy(title = event.title))
    }

private fun onDescriptionChange(model: Model, event: Event.OnDescriptionChanged): Upcoming =
    if (model.description == event.description) {
        noChange()
    } else {
        next(model.copy(description = event.description))
    }

private fun onTagActionClicked(model: Model): Upcoming =
    dispatch(setOf(Effect.UIEffect.ShowPriorityPicker(model.availablePriorities)))

private fun onDueDateDateAndTimeSelected(
    model: Model,
    event: Event.OnDueDateDateAndTimeSelected
): Upcoming {
    return if (model.dueDate?.date?.toLocalDate() == event.date &&
        model.dueDate.date.toLocalTime() == event.time
    ) {
        dispatch(
            setOf(
                Effect.UIEffect.DismissDueDatePicker,
            )
        )
    } else {
        dispatch(
            setOf(
                Effect.FormatDueDate(event.date, event.time),
                Effect.UIEffect.DismissDueDatePicker,
            )
        )
    }
}

private fun onPriorityChanged(model: Model, event: Event.OnPriorityChanged): Upcoming =
    if (model.priority == event.priority) {
        dispatch(setOf(Effect.UIEffect.DismissTaskPriorityPicker))
    } else {
        next(
            model.copy(priority = event.priority),
            setOf(
                Effect.UIEffect.DismissTaskPriorityPicker,
                Effect.GetAvailableTaskPriorities(event.priority)
            )
        )
    }

private fun onAttendeeAdded(model: Model, event: Event.OnAttendeeAdded): Upcoming =
    if (model.attendees.contains(event.attendee)) {
        noChange()
    } else {
        val attendees = model.attendees.toMutableSet()
        attendees.add(event.attendee)
        next(model.copy(attendees = attendees))
    }

private fun onAttendeeRemoved(model: Model, event: Event.OnAttendeeRemoved): Upcoming =
    if (!model.attendees.contains(event.attendee)) {
        noChange()
    } else {
        val attendees = model.attendees.toMutableSet()
        attendees.remove(event.attendee)
        next(model.copy(attendees = attendees))
    }

private fun onAttendeesActionClicked(): Upcoming =
    dispatch(setOf(Effect.UIEffect.ShowAttendeesPicker))

private fun onDueDateFormatted(model: Model, event: Event.OnDueDateFormatted): Upcoming =
    if (model.dueDate?.date != event.date &&
        model.dueDate?.displayableDate == event.formattedDate
    ) {
        noChange()
    } else {
        next(model.copy(dueDate = DueDate(event.date, event.formattedDate)))
    }

private fun onCalendarActionClicked(model: Model): Upcoming =
    dispatch(setOf(Effect.GetDueDatePickerInitialDate(model.dueDate)))

private fun onDueDatePickerInitialDateFound(
    event: Event.OnDueDatePickerInitialDateFound
): Upcoming = dispatch(setOf(Effect.UIEffect.ShowDueDateDatePicker(event.initialDate)))

private fun onTaskPrioritySelectorDismissed(): Upcoming =
    dispatch(setOf(Effect.UIEffect.DismissTaskPriorityPicker))

private fun onTaskPrioritiesFound(model: Model, event: Event.OnTaskPrioritiesFound): Upcoming {
    return if (model.availablePriorities == event.priorities) {
        noChange()
    } else {
        next(model.copy(availablePriorities = event.priorities))
    }
}

private fun onDismissAttendeePicker(): Upcoming =
    dispatch(setOf(Effect.UIEffect.DismissAttendeesPicker))
