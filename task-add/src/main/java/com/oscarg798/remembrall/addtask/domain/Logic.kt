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
    Event.OnBackClicked -> onBackClicked()
    Event.OnCloseClicked -> onCloseClicked()
    Event.OnTagActionClicked -> onTagActionClicked(model)
    is Event.OnTitleChanged -> onTitleChanged(model, event)
    is Event.OnDueDateChanged -> onDueDateChanged(model, event)
    Event.OnAttendeeActionClicked -> onAttendeesActionClicked()
    is Event.OnPriorityChanged -> onPriorityChanged(model, event)
    Event.OnCalendarActionClicked -> onCalendarActionClicked(model)
    is Event.OnDueDateFormatted -> onDueDateFormatted(model, event)
    is Event.OnAttendeesChanged -> onAttendeesChanges(model, event)
    is Event.OnDescriptionChanged -> onDescriptionChange(model, event)
    Event.OnTaskPrioritySelectorDismissed -> onTaskPrioritySelectorDismissed()
    is Event.OnDueDatePickerSelectedDateFound -> onDueDatePickerSelectedDateFound(event)
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

private fun onDueDateChanged(model: Model, event: Event.OnDueDateChanged): Upcoming =
    if (model.dueDate?.date == event.dueDate) {
        dispatch(setOf(Effect.UIEffect.DismissDueDatePicker))
    } else {
        dispatch(
            setOf(
                Effect.FormatDueDate(event.dueDate),
                Effect.UIEffect.DismissDueDatePicker
            )
        )
    }

private fun onPriorityChanged(model: Model, event: Event.OnPriorityChanged): Upcoming =
    if (model.priority == event.priority) {
        dispatch(setOf(Effect.UIEffect.DismissTaskPriorityPicker))
    } else {
        next(
            model.copy(priority = event.priority),
            setOf(Effect.UIEffect.DismissTaskPriorityPicker)
        )
    }

private fun onAttendeesChanges(model: Model, event: Event.OnAttendeesChanged): Upcoming =
    if (model.attendees == event.attendees) {
        dispatch(setOf(Effect.UIEffect.DismissAttendeesPicker))
    } else {
        next(
            model.copy(attendees = event.attendees),
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
    dispatch(setOf(Effect.GetDueDatePickerSelectedDate(model.dueDate)))

private fun onDueDatePickerSelectedDateFound(event: Event.OnDueDatePickerSelectedDateFound): Upcoming =
    dispatch(setOf(Effect.UIEffect.ShowDueDatePicker(event.selectedDate)))

private fun onTaskPrioritySelectorDismissed(): Upcoming =
    dispatch(setOf(Effect.UIEffect.DismissTaskPriorityPicker))