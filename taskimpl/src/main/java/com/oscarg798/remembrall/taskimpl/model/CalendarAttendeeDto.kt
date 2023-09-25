package com.oscarg798.remembrall.taskimpl.model

import com.oscarg798.remembrall.task.CalendarAttendee

data class CalendarAttendeeDto(val email: String) {

    constructor(calendarAttendee: CalendarAttendee) : this(calendarAttendee.email)

    fun toCalendarAttendee() = CalendarAttendee(email)
}