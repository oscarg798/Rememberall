package com.oscarg798.remembrall.widgetimpl.domain

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Next.noChange

internal typealias Upcoming = Next<Model, Effect>

internal fun update(
    model: Model,
    event: Event
): Upcoming = when (event) {
    Event.OnLoginClicked -> onLoginClicked()
    is Event.OnTaskClicked -> onTaskClicked(event)
    is Event.OnTaskFound -> onTasksFound(model, event)
    is Event.OnSessionStateFound -> onSessionStateFound(model, event)
    is Event.OnStateChanged -> onStateChanged(model, event)
}

private fun onStateChanged(model: Model, event: Event.OnStateChanged): Upcoming {
    return when (event.state) {
        RemembrallWidgetState.SessionStateChanged -> next(
            model.copy(sessionState = null, tasks = null),
            setOf(Effect.GetSessionState)
        )

        RemembrallWidgetState.DataRefreshRequired -> if (model.isUserLoggedIn()) {
            val user = (model.sessionState as Session.State.LoggedIn).user
            dispatch(setOf(Effect.GetTasks(user.email)))
        } else {
            next(
                model.copy(sessionState = null, tasks = null),
                setOf(Effect.GetSessionState)
            )
        }

        RemembrallWidgetState.Removed -> error("This is a termination signal, loop must be stoped")
    }
}

private fun onTasksFound(model: Model, event: Event.OnTaskFound): Upcoming {
    return if (model.tasks == event.task) {
        noChange()
    } else {
        next(model.copy(tasks = event.task))
    }
}

private fun onLoginClicked(): Upcoming = dispatch(
    setOf(Effect.UIEffect.NavigateToLogin)
)

private fun onSessionStateFound(model: Model, event: Event.OnSessionStateFound): Upcoming =
    if (model.sessionState == event.state) {
        noChange()
    } else if (event.state is Session.State.LoggedIn) {
        next(
            model.copy(sessionState = event.state), setOf(
                Effect.GetTasks(event.state.user.email)
            )
        )
    } else {
        next(model.copy(sessionState = event.state))
    }

private fun onTaskClicked(event: Event.OnTaskClicked): Upcoming = dispatch(
    setOf(Effect.UIEffect.NavigateToDetail(event.taskId))
)