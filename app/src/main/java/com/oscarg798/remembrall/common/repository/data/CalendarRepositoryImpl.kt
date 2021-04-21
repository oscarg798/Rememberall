package com.oscarg798.remembrall.common.repository.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.oscarg798.remembrall.common.exception.CalendarNotFoundException
import com.oscarg798.remembrall.common.model.Calendar
import com.oscarg798.remembrall.common.model.CalendarAttendee
import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remembrall.common.network.dto.EventAttendeeDto
import com.oscarg798.remembrall.common.network.dto.EventDto
import com.oscarg798.remembrall.common.network.dto.EventDtoDate
import com.oscarg798.remembrall.common.network.restclient.CalendarRestClient
import com.oscarg798.remembrall.common.repository.domain.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val preferences: SharedPreferences,
    private val calendarClient: CalendarRestClient,
    private val gson: Gson
) : CalendarRepository {

    override fun getSelectedCalendar(): Calendar {
        val calendarSaved =
            preferences.getString(SelectedCalendarKey, null) ?: throw CalendarNotFoundException()

        return gson.fromJson(calendarSaved, Calendar::class.java)
    }

    override fun saveSelectedCalendar(calendar: Calendar) {
        preferences.edit()
            .putString(SelectedCalendarKey, gson.toJson(calendar))
            .apply()
    }

    override suspend fun getCalendars(): Collection<Calendar> {
        return calendarClient.getCalendars(
            minAccessRole = MinAccessRole
        ).items.map {
            it.toCalendar()
        }
    }

    override suspend fun addTaskToCalendar(
        calendarId: String,
        calendarTask: CalendarRepository.CalendarTask,
        attendees: Set<String>?
    ): CalendarSyncInformation {
        calendarClient.add(
            calendarId,
            EventDto(
                id = calendarTask.id,
                summary = calendarTask.summary,
                startDate = EventDtoDate(calendarTask.startTimeDate),
                endDate = EventDtoDate(calendarTask.endTimeDate),
                description = calendarTask.description,
                attendees = attendees?.let {
                    attendees.map {
                        EventAttendeeDto(email = it)
                    }
                }
            )
        )

        return CalendarSyncInformation(
            calendarId = calendarId,
            calendarEventId = calendarTask.id,
            synced = true,
            attendees = attendees?.map {
                CalendarAttendee(it)
            }
        )
    }
}

private const val MinAccessRole = "writer"
private const val SelectedCalendarKey = "SelectedCalendarKey"
