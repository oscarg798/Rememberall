package com.oscarg798.remembrall.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import com.oscarg798.remembrall.common_auth.model.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.auth.ExternalAuthProvider
import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.login.domain.Model
import com.oscarg798.remembrall.login.domain.update
import com.oscarg798.remembrall.login.usecase.FinishLogIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val finishLogIn: FinishLogIn,
    private val authOptions: AuthOptions,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder,
    coroutineContextProvider: CoroutineContextProvider,
    private val externalAuthProvider: ExternalAuthProvider,
) : ViewModel(), CoroutineContextProvider by coroutineContextProvider {

    private val _model = MutableStateFlow(Model())
    val model: StateFlow<Model> = _model

    private val _uiEffects = MutableSharedFlow<Effect.UIEffect>(extraBufferCapacity = 1)
    val uiEffect: Flow<Effect.UIEffect> = _uiEffects

    private val events = MutableSharedFlow<Event>()
    private val effects = MutableSharedFlow<Effect>()

    init {
        viewModelScope.launch {
            events.map {
                update(_model.value, it)
            }.collectLatest {
                _model.value = it.first
                it.second.forEach { effect ->
                    effects.emit(effect)
                }
            }
        }

        viewModelScope.launch {
            effects.collectLatest {
                when (it) {
                    is Effect.UIEffect -> _uiEffects.emit(it)
                    is Effect.RequestExternalAuth -> {
                        onEvent(Event.OnExternalSignInFinished(externalAuthProvider.signIn()))
                    }

                    is Effect.FinishLogin -> runCatching {
                        finishLogIn()
                    }.fold({
                        onEvent(Event.OnSignedIn)
                    }, { error ->
                        if (error !is Exception) {
                            throw error
                        }
                        onEvent(Event.OnLoginError(error))
                    })

                    Effect.GetExternalSigningCredentials -> {
                        onEvent(
                            Event.OnExternalSignInOptionsFound(
                                googleAuthOptionsBuilder.buildFromAuthOptions(
                                    authOptions
                                ),
                                googleAuthOptionsBuilder.buildSignInRequest(authOptions)
                            )
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: Event) {
        viewModelScope.launch { events.emit(event) }
    }
}