package com.oscarg798.remembrall.profile.model

import com.remembrall.oscarg798.calendar.Calendar

data class ProfileInformation(
    val user: com.oscarg798.remembrall.user.User,
    val calendars: Collection<Calendar>,
    val selectedCalendar: String,
    val notificationsEnabled: Boolean
)
