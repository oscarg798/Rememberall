package com.oscarg798.remembrall.tasklist.usecase

import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun execute(): Collection<Task> =
        taskRepository.getTasks()
            .filter { !it.completed }
            .sortedWith { first, second ->
                first.priority.compareTo(second.priority)
            }
}
