package com.oscarg798.remembrall.calendarimpl.data

import com.oscarg798.remembrall.calendarimpl.data.model.CalendarListResponseDto
import com.oscarg798.remembrall.calendarimpl.data.model.EventDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CalendarRestClient {

    @GET("calendar/v3/users/me/calendarList")
    suspend fun getCalendars(
        @Query("minAccessRole") minAccessRole: String
    ): CalendarListResponseDto

    @POST("calendar/v3/calendars/{calendarId}/events")
    suspend fun add(
        @Path("calendarId") calendarId: String,
        @Body eventDto: EventDto
    )

    @PUT("calendar/v3/calendars/{calendarId}/events/{eventId}")
    suspend fun update(
        @Path("calendarId") calendarId: String,
        @Path("eventId") eventId: String,
        @Body eventDto: EventDto
    )
}
