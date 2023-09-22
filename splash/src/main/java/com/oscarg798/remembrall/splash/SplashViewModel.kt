package com.oscarg798.remembrall.splash

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.splash.usecase.IsUserLoggedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedIn,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<SplashViewModel.ViewState, SplashViewModel.Event>(
    ViewState()
), CoroutineContextProvider by coroutineContextProvider {

    fun init() = launch {
        update { it.copy(isLoading = true) }

        val isUserLoggedIn = isUserLoggedIn()

        _event.emit(
            if (isUserLoggedIn) {
                Event.NavigateToHome
            } else {
                Event.NavigateToLogin
            }
        )

        update { it.copy(isLoading = false) }
    }

    data class ViewState(val isLoading: Boolean = false)

    sealed interface Event {
        object NavigateToLogin : Event
        object NavigateToHome : Event
    }
}

