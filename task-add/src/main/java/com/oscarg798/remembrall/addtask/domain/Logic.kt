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
    Event.OnTaskSaved -> onBackClicked()
    Event.OnBackClicked -> onBackClicked()
    Event.OnCloseClicked -> onCloseClicked()
    Event.OnTagActionClicked -> onTagActionClicked(model)
    is Event.OnTitleChanged -> onTitleChanged(model, event)
    Event.OnSaveActionClicked -> onSaveActionClicked(model)
    Event.OnAttendeeActionClicked -> onAttendeesActionClicked()
    is Event.OnValidationError -> onValidationError(model, event)
    is Event.OnPriorityChanged -> onPriorityChanged(model, event)
    Event.OnCalendarActionClicked -> onCalendarActionClicked(model)
    is Event.OnDueDateFormatted -> onDueDateFormatted(model, event)
    is Event.OnAttendeesChanged -> onAttendeesChanges(model, event)
    is Event.OnDescriptionChanged -> onDescriptionChange(model, event)
    is Event.OnTaskPrioritiesFound -> onTaskPrioritiesFound(model, event)
    Event.OnTaskPrioritySelectorDismissed -> onTaskPrioritySelectorDismissed()
    is Event.OnDueDatePickerInitialDateFound -> onDueDatePickerInitialDateFound(event)
    is Event.OnDueDateDateAndTimeSelected -> onDueDateDateAndTimeSelected(model, event)
}

private fun onValidationError(model: Model, event: Event.OnValidationError): Upcoming {
    return if (model.loading) {
        next(model.copy(loading = false), setOf(Effect.UIEffect.ShowError(event.error)))
    } else {
        dispatch(setOf(Effect.UIEffect.ShowError(event.error)))
    }
}

private fun onSaveActionClicked(model: Model): Upcoming {
    return if (model.loading) {
        noChange()
    } else {

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

private fun onAttendeesChanges(model: Model, event: Event.OnAttendeesChanged): Upcoming =
    if (model.attendees.contains(event.attendee)) {
        dispatch(setOf(Effect.UIEffect.DismissAttendeesPicker))
    } else {
        val attendees = model.attendees.toMutableSet()
        attendees.add(event.attendee)
        next(
            model.copy(attendees = attendees),
            setOf(Effect.UIEffect.DismissAttendeesPicker)
        )
    }

private fun onAttendeesActionClicked(): Upcoming =
    dispatch(setOf(Effect.UIEffect.ShowAttendeesPicker))

private fun onDueDateFormatted(model: Model, event: Event.OnDueDateFormatted): Upcoming =
    if (model.dueDate?.date != event.date && model.dueDate?.displayableDate == event.formattedDate) {
        noChange()
    } else {
        next(model.copy(dueDate = DueDate(event.date, event.formattedDate)))
    }

private fun onCalendarActionClicked(model: Model): Upcoming =
    dispatch(setOf(Effect.GetDueDatePickerInitialDate(model.dueDate)))

private fun onDueDatePickerInitialDateFound(event: Event.OnDueDatePickerInitialDateFound): Upcoming =
    dispatch(setOf(Effect.UIEffect.ShowDueDateDatePicker(event.initialDate)))

private fun onTaskPrioritySelectorDismissed(): Upcoming =
    dispatch(setOf(Effect.UIEffect.DismissTaskPriorityPicker))

private fun onTaskPrioritiesFound(model: Model, event: Event.OnTaskPrioritiesFound): Upcoming {
    return if (model.availablePriorities == event.priorities) {
        noChange()
    } else {
        next(model.copy(availablePriorities = event.priorities))
    }
}