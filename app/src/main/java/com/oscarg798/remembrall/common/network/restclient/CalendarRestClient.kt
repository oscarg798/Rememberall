package com.oscarg798.remembrall.common.network.restclient

import com.oscarg798.remembrall.common.network.dto.CalendarListResponseDto
import com.oscarg798.remembrall.common.network.dto.EventDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarRestClient {

    @GET("calendar/v3/users/me/calendarList")
    suspend fun getCalendars(
        @Query("minAccessRole") minAccessRole: String
    ): CalendarListResponseDto

    @POST("calendar/v3/calendars/{calendarId}/events")
    suspend fun add(
        @Path("calendarId") calendarId: String,
        @Body eventDto: EventDto
    )
}
