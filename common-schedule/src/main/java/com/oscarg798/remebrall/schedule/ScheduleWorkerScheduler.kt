package com.oscarg798.remebrall.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class ScheduleWorkerScheduler @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val pendingIntent = PendingIntent.getBroadcast(
        context,
        ScheduleWorkerRequestCode,
        Intent(context, ScheduleWorkerReceiver::class.java),
        FLAG_CANCEL_CURRENT
    )

    fun schedule() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, AlarmHour)
            set(Calendar.MINUTE, AlarmMinutes)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}

private const val ScheduleWorkerRequestCode = 1010
private const val AlarmHour = 8
private const val AlarmMinutes = 30
