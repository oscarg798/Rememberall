package com.oscarg798.remembrall.schedule.usecase

import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTodayScheduleUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val getSignedInUserUseCase: GetSignedInUserUseCase
) {

    private val todayTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
    }.timeInMillis

    private val tomorrowTime = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
    }.timeInMillis

    suspend fun execute(): Collection<Task> {
        return taskRepository.getTasks(getSignedInUserUseCase.execute().email).filter {
            it.dueDate != null &&
                it.dueDate!! >= todayTime &&
                it.dueDate!! <= tomorrowTime &&
                !it.completed
        }
    }
}
