package com.oscarg798.remembrall.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleWorkerReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("Scheduler", "work scheduled ")
        workManager.enqueue(
            OneTimeWorkRequestBuilder<ScheduleWorker>()
                .setConstraints(
                    Constraints.Builder().setRequiresCharging(false)
                        .setRequiresBatteryNotLow(false)
                        .build()
                )
                .build()
        )
    }
}
