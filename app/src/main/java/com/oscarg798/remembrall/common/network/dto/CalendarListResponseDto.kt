package com.oscarg798.remembrall.common.network.dto

import com.google.gson.annotations.SerializedName

data class CalendarListResponseDto(
    @SerializedName("items")
    val items: List<CalendarDto>
)
