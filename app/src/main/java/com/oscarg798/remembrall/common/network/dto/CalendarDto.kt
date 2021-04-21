package com.oscarg798.remembrall.common.network.dto

import com.google.gson.annotations.SerializedName
import com.oscarg798.remembrall.common.model.Calendar

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
