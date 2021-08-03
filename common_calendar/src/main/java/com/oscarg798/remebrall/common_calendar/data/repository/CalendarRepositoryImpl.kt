package com.oscarg798.remebrall.common_calendar.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.oscarg798.remebrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common.model.CalendarAttendee
import com.oscarg798.remembrall.common.model.CalendarSyncInformation
import com.oscarg798.remebrall.common_calendar.data.model.EventAttendeeDto
import com.oscarg798.remebrall.common_calendar.data.model.EventDto
import com.oscarg798.remebrall.common_calendar.data.model.EventDtoDate
import com.oscarg798.remebrall.common_calendar.data.restclient.CalendarRestClient
import com.oscarg798.remebrall.common_calendar.domain.repository.CalendarRepository
import com.oscarg798.remebrall.common_calendar.exception.CalendarNotFoundException
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

    override suspend fun updateCalendarEvent(
        calendarId: String,
        calendarEventId: String,
        calendarTask: CalendarRepository.CalendarTask,
        attendees: Set<String>?
    ): CalendarSyncInformation {
        calendarClient.update(
            calendarId, calendarEventId,
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
