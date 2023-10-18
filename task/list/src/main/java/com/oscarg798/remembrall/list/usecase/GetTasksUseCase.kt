package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal interface GetTasksUseCase : suspend () -> Flow<Collection<Task>>

@Reusable
internal class GetTasksUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    private val session: Session
) : GetTasksUseCase {

    override suspend fun invoke(): Flow<Collection<Task>> {
        System.err.println("OSCAR_TASKS getting tasks from usecase")
        return   taskRepository.streamTasks(getLoggedInUser().email)
            .map { tasks ->
                tasks.filter { task -> !task.completed }
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
    }


    private suspend fun getLoggedInUser() =
        (session.getLoggedInState() as? Session.State.LoggedIn)?.user
            ?: error("User must be logged in to get tasks")

}
