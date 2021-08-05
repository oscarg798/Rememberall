package com.oscarg798.remembrall.profile.usecase

import com.oscarg798.remembrall.common_calendar.domain.usecase.GetCalendars
import com.oscarg798.remembrall.common_calendar.domain.usecase.GetSelectedCalendar
import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import com.oscarg798.remembrall.profile.model.ProfileInformation
import javax.inject.Inject

class GetProfileInformationUseCase @Inject constructor(
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val getSelectedCalendar: GetSelectedCalendar,
    private val getCalendars: GetCalendars,
    private val preferenceRepository: PreferenceRepository
) {

    suspend fun execute(): ProfileInformation {
        val user = getSignedInUserUseCase.execute()
        val selectedCalendar = getSelectedCalendar()

        return ProfileInformation(
            user,
            getCalendars(),
            selectedCalendar.id,
            preferenceRepository.getNotificationValue()
        )
    }
}
