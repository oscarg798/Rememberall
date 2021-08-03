package com.oscarg798.remebrall.schedule.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.oscarg798.remebrall.schedule.R
import com.oscarg798.remebrall.schedule.ScheduleWorker
import com.oscarg798.remebrall.schedule.util.PendingIntentFinder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pendingIntentFinder: PendingIntentFinder,
    private val notificationManager: NotificationManager
) {

    fun schedule(type: Type) {
        createNotificationChannel(notificationManager)

        notificationManager.notify(
            NotificationId,
            when (type) {
                is Type.BigText -> buildBigNotification(type)
                is Type.Small -> buildSmallNotification(type)
            }.setContentIntent(getPendingIntent())
                .build()
        )

        Log.i("Scheduler", "Notification sent")
    }

    private fun buildSmallNotification(type: Type): NotificationCompat.Builder =
        getBaseNotificationBuilder(type)
            .setContentText(type.content)
            .setSubText(type.subtext)
            .setAutoCancel(false)

    private fun buildBigNotification(type: Type.BigText): NotificationCompat.Builder =
        getBaseNotificationBuilder(type)
            .setContentText(type.subtext)
            .setSubText(type.subtext)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(type.bigText)
            )

    private fun getBaseNotificationBuilder(type: Type) =
        NotificationCompat.Builder(context, ChannelId)
            .setContentTitle(type.title)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_agenda)

    private fun getPendingIntent() = pendingIntentFinder.getPendingIntent(context, NotificationId)

    private fun createNotificationChannel(
        notificationManager: NotificationManager,
    ): NotificationChannel {
        return NotificationChannel(
            ChannelId,
            ScheduleWorker.WorkName,
            NotificationManager.IMPORTANCE_HIGH
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

    sealed class Type(
        val title: String,
        val content: String,
        val subtext: String,
        val subTitle: String,
    ) {
        class BigText(
            title: String,
            content: String,
            subtext: String,
            subTitle: String,
            val bigText: String
        ) : Type(title = title, content = content, subtext = subtext, subTitle = subTitle)

        class Small(
            title: String,
            content: String,
            subtext: String,
            subTitle: String
        ) : Type(title = title, content = content, subtext = subtext, subTitle = subTitle)
    }
}

private const val NotificationId = 1
private const val ChannelId = "RemembrallSchedule"
