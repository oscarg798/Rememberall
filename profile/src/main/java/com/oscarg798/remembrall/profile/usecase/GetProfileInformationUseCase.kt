package com.oscarg798.remembrall.profile.usecase

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import com.oscarg798.remembrall.profile.model.ProfileInformation
import com.remembrall.oscarg798.calendar.CalendarRepository
import javax.inject.Inject

class GetProfileInformationUseCase @Inject constructor(
    private val session: Session,
    private val calendarRepository: CalendarRepository,
    private val preferenceRepository: PreferenceRepository
) {

    suspend fun execute(): ProfileInformation {
        val user = (session.getSessionState() as? Session.State.LoggedIn)?.user
            ?: throw IllegalStateException(
                "Can not get profile information without an user logged in"
            )

        val selectedCalendar = calendarRepository.getSelectedCalendar()

        return ProfileInformation(
            user = user,
            calendars = calendarRepository.getCalendars(),
            selectedCalendar = selectedCalendar.id,
            notificationsEnabled = preferenceRepository.getNotificationValue()
        )
    }
}
