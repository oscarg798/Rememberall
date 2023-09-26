package com.oscarg798.remembrall.calendarimpl.data.model

import com.google.gson.annotations.SerializedName

internal class EventDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("start")
    val startDate: EventDtoDate,
    @SerializedName("end")
    val endDate: EventDtoDate,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("attendees")
    val attendees: List<EventAttendeeDto>? = null
)

internal class EventAttendeeDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("organizer")
    val organizer: Boolean = false,
    @SerializedName("self")
    val self: Boolean = true,
    @SerializedName("responseStatus")
    val responseStatus: String = "needsAction"
)

internal class EventDtoDate(@SerializedName("dateTime") val date: String)
