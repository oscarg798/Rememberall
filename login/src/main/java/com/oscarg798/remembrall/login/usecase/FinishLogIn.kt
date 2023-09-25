package com.oscarg798.remembrall.login.usecase

import com.oscarg798.remembrall.common_calendar.domain.usecase.GetSelectedCalendar
import javax.inject.Inject

class FinishLogIn @Inject constructor(
    private val getSelectedCalendar: GetSelectedCalendar
) {

    suspend operator fun invoke() {
        getSelectedCalendar()
    }
}