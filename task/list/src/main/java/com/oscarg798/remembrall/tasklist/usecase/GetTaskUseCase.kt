package com.oscarg798.remembrall.tasklist.usecase

import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val getSignedInUserUseCase: GetSignedInUserUseCase
) {

    suspend fun execute(): Collection<Task> =
        taskRepository.getTasks(getSignedInUserUseCase.execute().email).filter { !it.completed }
            .sortedWith { first, second ->
                if (first.priority == null && second.priority == null) {
                    0
                } else if (first.priority != null && second.priority == null) {
                    1
                } else if (first.priority == null) {
                    -1
                } else {
                    first!!.priority!!.compareTo(second!!.priority!!)
                }
            }
}
