package com.oscarg798.remembrall.profile

import androidx.lifecycle.viewModelScope
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.profile.model.ProfileInformation
import com.oscarg798.remembrall.profile.usecase.GetProfileInformationUseCase
import com.oscarg798.remembrall.profile.usecase.LogOutUseCase
import com.oscarg798.remembrall.profile.usecase.SaveNotificationValueUseCase
import com.oscarg798.remembrall.profile.usecase.SetCalendarSelectedUseCase
import com.remembrall.oscarg798.calendar.Calendar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileInformationUseCase: GetProfileInformationUseCase,
    private val setCalendarSelectedUseCase: SetCalendarSelectedUseCase,
    private val saveNotificationValueUseCase: SaveNotificationValueUseCase,
    private val logOutUseCase: LogOutUseCase,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<ProfileViewModel.ViewState, ProfileViewModel.Event>(
    ViewState()
),
    CoroutineContextProvider by coroutineContextProvider {

    fun getProfileInformation() {
        launch {
            update { it.copy(loading = true) }
            runCatching {
                withContext(io) {
                    getProfileInformationUseCase.execute()
                }
            }.onSuccess { profileInformation ->
                update {
                    it.copy(
                        loading = false,
                        profileInformation = profileInformation
                    )
                }
            }.onFailure { error ->
                if (error !is Exception) throw error

                update {
                    it.copy(
                        loading = false,
                        profileInformation = null
                    )
                }
                _event.emit(Event.NavigateToLogin)
            }
        }
    }

    fun onCalendarSelected(calendar: Calendar) {
        launch {
            withContext(io) {
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
                withContext(io) {
                    logOutUseCase.execute()
                }
            }.onSuccess {
                update { it.copy(loading = false, profileInformation = null) }
                _event.emit(Event.NavigateToLogin)
            }.onFailure {
                update { it.copy(loading = false) }
            }
        }
    }

    fun onNotificationValueChange(enabled: Boolean) {
        viewModelScope.launch {
            update { it.copy(loading = true) }

            withContext(io) {
                saveNotificationValueUseCase.execute(enabled)
            }

            getProfileInformation()
        }
    }

    data class ViewState(
        val loading: Boolean = true,
        val profileInformation: ProfileInformation? = null
    )

    sealed interface Event {
        object NavigateToLogin : Event
    }
}
