package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.task.TaskRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class RemoveTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    suspend fun execute(id: String) {
        taskRepository.removeTask(id)
    }
}
