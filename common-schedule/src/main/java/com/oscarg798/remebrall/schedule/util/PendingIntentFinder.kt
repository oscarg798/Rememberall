package com.oscarg798.remebrall.schedule.util

import android.app.PendingIntent
import android.content.Context

interface PendingIntentFinder {

    fun getPendingIntent(context: Context, notificationId: Int): PendingIntent
}