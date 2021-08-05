package com.oscarg798.remembrall.profile.model

import com.oscarg798.remembrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common.model.User

data class ProfileInformation(
    val user: User,
    val calendars: Collection<Calendar>,
    val selectedCalendar: String,
    val notificationsEnabled: Boolean
)
