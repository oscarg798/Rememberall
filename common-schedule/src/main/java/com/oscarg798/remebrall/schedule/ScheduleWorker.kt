package com.oscarg798.remebrall.schedule

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.oscarg798.remebrall.schedule.usecase.VerifyAgendaUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ScheduleWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val verifyAgendaUseCase: VerifyAgendaUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        verifyAgendaUseCase.execute()

        return Result.success()
    }

    companion object {
        const val WorkName = "DailyScheduleWorker"
        const val WorkTag = "DailyScheduleWorker"
    }
}
