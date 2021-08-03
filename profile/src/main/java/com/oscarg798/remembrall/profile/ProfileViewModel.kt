package com.oscarg798.remembrall.profile

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remebrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import com.oscarg798.remembrall.common_auth.model.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.profile.usecase.GetProfileInformationUseCase
import com.oscarg798.remembrall.profile.usecase.LogOutUseCase
import com.oscarg798.remembrall.profile.usecase.SaveNotificationValueUseCase
import com.oscarg798.remembrall.profile.usecase.SetCalendarSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authOptions: AuthOptions,
    private val getProfileInformationUseCase: GetProfileInformationUseCase,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder,
    private val setCalendarSelectedUseCase: SetCalendarSelectedUseCase,
    private val saveNotificationValueUseCase: SaveNotificationValueUseCase,
    private val logOutUseCase: LogOutUseCase,
    coroutineContextProvider: CoroutineContextProvider
) :
    AbstractViewModel<ProfileViewModel.ViewState, ProfileViewModel.Event>(
        ViewState(),
        coroutineContextProvider
    ) {

    fun getProfileInformation() {
        viewModelScope.launch {
            update { it.copy(loading = true, error = null) }
            runCatching {
                withContext(coroutineContextProvider.io) {
                    getProfileInformationUseCase.execute()
                }
            }.onSuccess { profileInformation ->
                update {
                    it.copy(
                        loading = false,
                        profileInformation = profileInformation,
                        error = null
                    )
                }
            }.onFailure {
                update {
                    it.copy(
                        loading = false,
                        profileInformation = null,
                        error = null
                    )
                }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            update { it.copy(loading = true) }
            val signInOptions = withContext(coroutineContextProvider.io) {
                googleAuthOptionsBuilder.buildFromAuthOptions(authOptions)
            }

            _event.tryEmit(Event.RequestAuth(signInOptions))
        }
    }

    fun onAuth() {
        getProfileInformation()
    }

    fun onCalendarSelected(calendar: Calendar) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                setCalendarSelectedUseCase.execute(calendar)
            }

            update {
                it.copy(
                    profileInformation = it.profileInformation!!
                        .copy(selectedCalendar = calendar.id)
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            update { it.copy(loading = true) }

            runCatching {
                withContext(coroutineContextProvider.io) {
                    logOutUseCase.execute()
                }
            }.onSuccess {
                update { it.copy(loading = false, profileInformation = null) }
            }.onFailure {
                update { it.copy(loading = false) }
            }
        }
    }

    fun onNotificationValueChange(enabled: Boolean) {
        viewModelScope.launch {
            update { it.copy(loading = true) }

            withContext(coroutineContextProvider.io) {
                saveNotificationValueUseCase.execute(enabled)
            }

            getProfileInformation()
        }
    }

    data class ViewState(
        val loading: Boolean = true,
        val profileInformation: com.oscarg798.remembrall.profile.model.ProfileInformation? = null,
        val error: Exception? = null
    )

    sealed class Event {
        data class RequestAuth(val options: GoogleSignInOptions) : Event()
    }
}
