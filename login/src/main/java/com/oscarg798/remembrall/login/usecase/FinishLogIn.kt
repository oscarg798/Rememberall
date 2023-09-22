package com.oscarg798.remembrall.login.usecase

import com.oscarg798.remembrall.common_calendar.domain.usecase.GetSelectedCalendar
import com.oscarg798.remembrall.common.auth.AuthRepository
import javax.inject.Inject

class FinishLogIn @Inject constructor(
    private val getSelectedCalendar: GetSelectedCalendar
) {

    suspend operator fun invoke() {
        getSelectedCalendar()
    }
}