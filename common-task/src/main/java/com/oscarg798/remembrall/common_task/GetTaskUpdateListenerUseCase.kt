package com.oscarg798.remembrall.common_task

import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import javax.inject.Inject

class GetTaskUpdateListenerUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    fun execute() = taskRepository.taskUpdateListener
}
