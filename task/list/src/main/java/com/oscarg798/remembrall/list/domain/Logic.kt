package com.oscarg798.remembrall.list.domain

import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.domain.model.Model
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Next.noChange

internal typealias Upcoming = Next<Model, Effect>

internal fun update(
    model: Model,
    event: Event
): Upcoming = when (event) {
    Event.OnAddClicked -> onAddClicked()
    is Event.OnTasksClicked -> onTaskClicked(event)
    is Event.OnTasksFound -> onTasksFound(model, event)
    is Event.OnScrollIndexFound -> onScrollIndexFound(event)
    is Event.OnTaskOptionsFound -> onTaskOptionsFound(event)
    is Event.OnTaskOptionsClicked -> onTasksOptionsClicked(event)
}

private fun onAddClicked(): Upcoming = dispatch(
    setOf(Effect.UIEffect.NavigateToAdd)
)

private fun onTaskOptionsFound(event: Event.OnTaskOptionsFound): Upcoming = dispatch(
    setOf(Effect.UIEffect.ShowOptions(event.options))
)

private fun onScrollIndexFound(event: Event.OnScrollIndexFound): Upcoming = dispatch(
    setOf(Effect.UIEffect.ScrollToItem(event.index))
)

private fun onTasksOptionsClicked(event: Event.OnTaskOptionsClicked): Upcoming = dispatch(
    setOf(Effect.GetTaskOptions(event.task))
)

private fun onTaskClicked(event: Event.OnTasksClicked): Upcoming = dispatch(
    setOf(Effect.UIEffect.NavigateToDetail(event.taskId))
)

private fun onTasksFound(model: Model, event: Event.OnTasksFound): Upcoming {
    return if (model.tasks == event.tasks) {
        noChange()
    } else {
        next(model.copy(tasks = event.tasks))
    }
}