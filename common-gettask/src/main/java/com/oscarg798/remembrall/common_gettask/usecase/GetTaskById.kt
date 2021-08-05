package com.oscarg798.remembrall.common_gettask.usecase

import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import javax.inject.Inject

class GetTaskById @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun execute(taskId: String) = taskRepository.getTask(taskId)
}
