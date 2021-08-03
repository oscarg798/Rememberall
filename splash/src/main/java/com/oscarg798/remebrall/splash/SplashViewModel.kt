package com.oscarg798.remebrall.splash

import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remebrall.splash.usecase.VerifyUserSectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val verifyUserSectionUseCase: VerifyUserSectionUseCase,
    coroutineContextProvider: CoroutineContextProvider
) :
    AbstractViewModel<SplashViewModel.ViewState, SplashViewModel.NavigateToHome>(
        ViewState(),
        coroutineContextProvider
    ) {

    fun init() {
        val launchTime = Date()
        viewModelScope.launch {

            withContext(coroutineContextProvider.io) {
                runCatching {
                    verifyUserSectionUseCase.execute()
                }
                validateElapsedTime(launchTime)
            }

            _event.tryEmit(NavigateToHome)
        }
    }

    private suspend fun validateElapsedTime(launchTime: Date) {
        val elapsedTime = Date().time - launchTime.time
        if (elapsedTime < DismissTimeInMillis) {
            delay(DismissTimeInMillis - elapsedTime)
        }
    }

    class ViewState(val isLoading: Boolean = false)
    object NavigateToHome
}

private const val DismissTimeInMillis = 2 * 1000
