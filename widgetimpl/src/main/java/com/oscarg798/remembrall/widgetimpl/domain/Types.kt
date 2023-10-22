package com.oscarg798.remembrall.widgetimpl.domain

import androidx.annotation.StringRes
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.widget.RemembrallWidgetState

internal data class Model(
    val sessionState: Session.State? = null,
    val tasks: List<DisplayableTask>? = null
) {

    fun isUserLoggedIn() = sessionState is Session.State.LoggedIn
}

internal sealed interface Event {

    data object OnLoginClicked : Event
    data class OnTaskFound(val task: List<DisplayableTask>) : Event
    data class OnTaskClicked(val taskId: String) : Event
    data class OnSessionStateFound(val state: Session.State) : Event
    data class OnStateChanged(val state: RemembrallWidgetState): Event
}

internal sealed interface Effect {

    data object GetSessionState : Effect
    data class GetTasks(val user: String) : Effect

    sealed interface UIEffect : Effect {
        data object NavigateToLogin : UIEffect
        data class NavigateToDetail(val taskId: String) : UIEffect
    }
}

internal data class DisplayableTask(
    val id: String,
    val title: String,
    val owned: Boolean,
    val description: String? = null,
    val dueDate: String? = null,
    @StringRes val priorityLabelResource: Int? = null,
)