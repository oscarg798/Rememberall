package com.oscarg798.remembrall.tasklist.usecase

import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class RemoveTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    suspend fun execute(id: String) {
        val task = taskRepository.getTask(id)
        taskRepository.removeTask(id)
        taskRepository.onTaskUpdated(task = task)
    }
}
