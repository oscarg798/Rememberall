package com.oscarg798.remembrall.profile.usecase

import android.util.Log
import com.oscarg798.remembrall.schedule.ScheduleWorkerScheduler
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import javax.inject.Inject

class SaveNotificationValueUseCase @Inject constructor(
    private val repository: PreferenceRepository,
    private val scheduleWorkerScheduler: ScheduleWorkerScheduler
) {

    fun execute(enabled: Boolean) {
        repository.saveNotificationValue(enabled)

        if (!enabled) {
            return
        }

        scheduleWorkerScheduler.schedule()

        Log.i("Scheduler", "Work manager scheduled to run")
    }
}
