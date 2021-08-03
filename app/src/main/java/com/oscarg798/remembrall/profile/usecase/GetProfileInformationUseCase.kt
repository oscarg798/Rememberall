package com.oscarg798.remembrall.profile.usecase

import com.oscarg798.remembrall.common.exception.CalendarNotFoundException
import com.oscarg798.remembrall.common.model.Calendar
import com.oscarg798.remembrall.common.repository.domain.CalendarRepository
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import com.oscarg798.remembrall.common.usecase.GetCalendarListUseCase
import com.oscarg798.remembrall.common_auth.usecase.GetSignedInUserUseCase
import com.oscarg798.remembrall.profile.ui.ProfileInformation
import javax.inject.Inject

class GetProfileInformationUseCase @Inject constructor(
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val getCalendarListUseCase: GetCalendarListUseCase,
    private val calendarRepository: CalendarRepository,
    private val preferenceRepository: PreferenceRepository
) {

    suspend fun execute(): ProfileInformation {
        val user = getSignedInUserUseCase.execute()
        val calendars = getCalendarListUseCase.execute()
        val selectedCalendar = getSelectedCalendar(calendars)

        return ProfileInformation(
            user,
            calendars,
            selectedCalendar.id,
            preferenceRepository.getNotificationValue()
        )
    }

    private fun getSelectedCalendar(calendars: Collection<Calendar>): Calendar {
        return runCatching {
            calendarRepository.getSelectedCalendar()
        }.getOrElse { cause ->
            when (cause) {
                is CalendarNotFoundException -> calendars.firstOrNull {
                    it.isPrimary
                } ?: calendars.first()
                else -> throw cause
            }
        }.also {
            calendarRepository.saveSelectedCalendar(it)
        }
    }
}
