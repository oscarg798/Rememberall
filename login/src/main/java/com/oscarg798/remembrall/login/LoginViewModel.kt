package com.oscarg798.remembrall.login

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_auth.exception.AuthException
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import com.oscarg798.remembrall.common_auth.model.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.login.usecase.FinishLogIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val finishLogIn: FinishLogIn,
    private val authOptions: AuthOptions,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder,
    private val stringProvider: StringProvider,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<LoginViewModel.ViewState, LoginViewModel.Event>(
    ViewState()
), CoroutineContextProvider by coroutineContextProvider {

    fun signIn() = launch {
        update { it.copy(loading = true) }

        val signInOptions = withContext(io) {
            googleAuthOptionsBuilder.buildFromAuthOptions(authOptions)
        }

        update { it.copy(loading = false) }
        _event.tryEmit(Event.RequestAuth(signInOptions))
    }

    fun onExternalLogin() = launch {
        update { it.copy(loading = true) }
        runCatching {
            withContext(io) {
                finishLogIn()
            }
        }.fold({
            update { it.copy(loading = false) }
            _event.tryEmit(Event.NavigateToHome)
        }, { error ->
            if (error !is Exception) {
                throw error
            }
            update { it.copy(loading = false) }
            _event.tryEmit(
                when (error) {
                    is AuthException -> Event.ShowErrorMessage.LoginError(stringProvider.get(R.string.login_error))
                    else -> Event.ShowErrorMessage.UnknownError(stringProvider.get(R.string.generic_error))
                }
            )
        })
    }

    data class ViewState(
        val isLoggedIn: Boolean = false,
        val loading: Boolean = false
    )

    sealed interface Event {

        data class RequestAuth(val googleSignInOptions: GoogleSignInOptions) : Event
        sealed class ShowErrorMessage(val message: String) : Event {
            class LoginError(message: String) : ShowErrorMessage(message)
            class UnknownError(message: String) : ShowErrorMessage(message)
        }

        object NavigateToHome : Event
    }
}