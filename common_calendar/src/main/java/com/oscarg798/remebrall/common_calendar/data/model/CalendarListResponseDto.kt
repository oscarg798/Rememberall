package com.oscarg798.remebrall.common_calendar.data.model

import com.google.gson.annotations.SerializedName

data class CalendarListResponseDto(
    @SerializedName("items")
    val items: List<CalendarDto>
)
