package com.oscarg798.remembrall.common

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.oscarg798.remebrall.schedule.util.PendingIntentFinder
import com.oscarg798.remembrall.home.HomeActivity
import javax.inject.Inject

class HomeActivityPendingIntentFinder @Inject constructor() : PendingIntentFinder {

    override fun getPendingIntent(context: Context, notificationId: Int): PendingIntent =
        PendingIntent.getActivity(
            context,
            notificationId,
            Intent(context, HomeActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
}
