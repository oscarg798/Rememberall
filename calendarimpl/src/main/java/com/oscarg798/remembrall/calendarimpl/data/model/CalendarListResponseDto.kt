package com.oscarg798.remembrall.calendarimpl.data.model

import com.google.gson.annotations.SerializedName

internal data class CalendarListResponseDto(
    @SerializedName("items")
    val items: List<CalendarDto>
)