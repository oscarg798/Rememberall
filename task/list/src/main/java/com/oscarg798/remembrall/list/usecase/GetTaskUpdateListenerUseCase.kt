package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.task.TaskRepository
import javax.inject.Inject

@Deprecated("This must be elsewhere")
class GetTaskUpdateListenerUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    fun execute() = taskRepository.taskUpdateListener
}
