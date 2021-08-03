package com.oscarg798.remebrall.common_calendar.data.model

import com.google.gson.annotations.SerializedName
import com.oscarg798.remebrall.common_calendar.domain.model.Calendar

data class CalendarDto(
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
