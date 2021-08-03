package com.oscarg798.remebrall.schedule.usecase

import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import javax.inject.Inject
import javax.inject.Singleton
import java.util.Calendar

@Singleton
class GetTodayScheduleUseCase @Inject constructor(private val taskRepository: TaskRepository) {

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
        return taskRepository.getTasks().filter {
            it.dueDate != null &&
                it.dueDate!! >= todayTime &&
                it.dueDate!! <= tomorrowTime &&
                !it.completed
        }
    }
}
