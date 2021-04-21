package com.oscarg798.remembrall.common.network.dto

import com.oscarg798.remembrall.common.model.CalendarAttendee

data class CalendarAttendeeDto(val email: String) {

    constructor(calendarAttendee: CalendarAttendee) : this(calendarAttendee.email)

    fun toCalendarAttendee() = CalendarAttendee(email)
}
