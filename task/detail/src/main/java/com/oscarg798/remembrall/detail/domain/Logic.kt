package com.oscarg798.remembrall.detail.domain

import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Next.noChange

internal typealias Upcoming = Next<Model, Effect>

internal fun update(
    model: Model,
    event: Event
): Upcoming = when (event) {
    Event.OnBackButtonClicked -> onBackButtonClicked()
    Event.OnErrorDeletingTask -> onErrorDeletingTasks(model)
    Event.OnEditActionClicked -> onEditActionClicked(model)
    Event.OnTaskMarkedAsCompleted -> onTaskMarkedAsCompleted(model)
    Event.OnDeleteButtonClicked -> onDeleteButtonClicked(model)
    is Event.OnDisplayableTaskFound -> onDisplayableTaskFound(model, event)
}

private fun onErrorDeletingTasks(model: Model): Upcoming {
    return next(
        model.copy(loading = false),
        setOf(Effect.UIEffect.ShowError(Error.ErrorDeletingTask))
    )
}

private fun onTaskMarkedAsCompleted(model: Model): Upcoming {
    return next(model.copy(loading = false), setOf(Effect.UIEffect.CloseScreen))
}

private fun onDeleteButtonClicked(model: Model): Upcoming {
    val effects = setOf(Effect.MarkTaskAsCompleted(model.taskId))
    return if (model.loading) {
        dispatch(effects)
    } else {
        next(model.copy(loading = true), effects)
    }
}

private fun onBackButtonClicked(): Upcoming = dispatch(setOf(Effect.UIEffect.CloseScreen))

private fun onDisplayableTaskFound(model: Model, event: Event.OnDisplayableTaskFound): Upcoming {
    return if (model.task == event.task && !model.loading) {
        noChange()
    } else if (model.task == event.task) {
        next(model.copy(loading = false))
    } else {
        next(model.copy(task = event.task, loading = false))
    }
}

private fun onEditActionClicked(model: Model): Upcoming = dispatch(
    setOf(
        Effect.UIEffect.NavigateToEdit(model.taskId)
    )
)