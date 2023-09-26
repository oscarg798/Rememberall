package com.oscarg798.remembrall.calendarimpl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.calendarimpl.data.CalendarRestClient
import com.oscarg798.remembrall.calendarimpl.data.model.EventAttendeeDto
import com.oscarg798.remembrall.calendarimpl.data.model.EventDto
import com.oscarg798.remembrall.calendarimpl.data.model.EventDtoDate
import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.task.CalendarSyncInformation
import com.remembrall.oscarg798.calendar.Calendar
import com.remembrall.oscarg798.calendar.CalendarRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

internal
class CalendarRepositoryImpl @Inject constructor(
    private val gson: Gson,
    private val preferences: SharedPreferences,
    private val calendarClient: CalendarRestClient,
    private val coroutinesContextProvider: CoroutineContextProvider,
) : CalendarRepository {

    override suspend fun getSelectedCalendar(): Calendar =
        withContext(coroutinesContextProvider.io) {
            val calendarSaved = preferences.getString(SelectedCalendarKey, null)

            if (calendarSaved != null) {
                return@withContext gson.fromJson(calendarSaved, Calendar::class.java)
            }

            val calendar = getCalendars().firstOrNull()
                ?: throw NullPointerException("User does not have any calendar")

            saveSelectedCalendar(calendar)
            calendar
        }

    override suspend fun saveSelectedCalendar(calendar: Calendar) =
        withContext(coroutinesContextProvider.io) {
            preferences.edit()
                .putString(SelectedCalendarKey, gson.toJson(calendar))
                .apply()
        }

    override suspend fun getCalendars(): Collection<Calendar> =
        withContext(coroutinesContextProvider.io) {
            calendarClient.getCalendars(
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
        withContext(coroutinesContextProvider.io) {
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
        }

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
    ): com.oscarg798.remembrall.task.CalendarSyncInformation {
        withContext(coroutinesContextProvider.io) {
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
        }

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