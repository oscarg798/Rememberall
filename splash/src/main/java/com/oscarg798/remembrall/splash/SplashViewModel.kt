package com.oscarg798.remembrall.splash

import com.oscarg798.remembrall.splash.usecase.isUserLoggedIn
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.withContext

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserLoggedIn: isUserLoggedIn,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<SplashViewModel.ViewState, SplashViewModel.Event>(
    ViewState()
), CoroutineContextProvider by coroutineContextProvider {

    fun init() = launch {
        update { it.copy(isLoading = true) }

        val isUserLoggedIn = withContext(io) {
            isUserLoggedIn()
        }

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

