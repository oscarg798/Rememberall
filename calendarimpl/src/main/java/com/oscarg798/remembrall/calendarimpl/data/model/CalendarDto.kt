package com.oscarg798.remembrall.calendarimpl.data.model

import com.google.gson.annotations.SerializedName
import com.remembrall.oscarg798.calendar.Calendar

internal data class CalendarDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("summary")
    val name: String,
    @SerializedName("primary")
    val primary: Boolean?
) {

    fun toCalendar() = Calendar(
        id = id,
        name = name,
        isPrimary = primary ?: false
    )
}
