package com.oscarg798.remembrall.common_gettask.usecase

import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import javax.inject.Inject

class GetTaskById @Inject constructor(
    private val taskRepository: TaskRepository,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
) {

    suspend fun execute(taskId: String): Task {
        val user = getSignedInUserUseCase.execute()
        val task = taskRepository.getTask(taskId)
        return task.copy(
            owned = user.email == task.owner
        )
    }
}
