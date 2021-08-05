package com.oscarg798.remembrall.common

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.oscarg798.remembrall.home.HomeActivity
import com.oscarg798.remembrall.schedule.util.PendingIntentFinder
import javax.inject.Inject

@ExperimentalPagerApi
class HomeActivityPendingIntentFinder @Inject constructor() : PendingIntentFinder {

    override fun getPendingIntent(context: Context, notificationId: Int): PendingIntent =
        PendingIntent.getActivity(
            context,
            notificationId,
            Intent(context, HomeActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
}
