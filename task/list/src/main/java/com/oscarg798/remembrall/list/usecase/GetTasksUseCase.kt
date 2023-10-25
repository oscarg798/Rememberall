package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.task.TaskRepository.QueryOperation
import com.oscarg798.remembrall.task.TaskRepository.TaskQuery
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
    private object TaskComparator : Comparator<Task> {
        override fun compare(first: Task, second: Task): Int {
            return if (first.priority == null && second.priority == null) {
                0
            } else if (first.priority != null && second.priority == null) {
                1
            } else if (first.priority == null) {
                -1
            } else {
                first.priority!!.compareTo(second.priority!!)
            }
        }
    }

    override suspend fun invoke(): Flow<Collection<Task>> {
        val user = getLoggedInUser().email
        return taskRepository.streamTasks(
            listOf(
                TaskQuery.UserEquals(user),
                TaskQuery.Completed(false)
            ), QueryOperation.And
        )
            .map { tasks ->
                tasks.sortedWith(TaskComparator)
            }
    }

    private suspend fun getLoggedInUser() =
        (session.getSessionState() as? Session.State.LoggedIn)?.user
            ?: error("User must be logged in to get tasks")

}
