package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event.OnTaskPrioritiesFound
import com.oscarg798.remembrall.task.TaskPriority
import javax.inject.Inject
import kotlinx.coroutines.withContext

internal interface GetAvailableTaskPriorities :
    suspend (Effect.GetAvailableTaskPriorities) -> OnTaskPrioritiesFound

internal class GetAvailableTaskPrioritiesImpl @Inject constructor(
    private val coroutinesContextProvider: CoroutineContextProvider
) : GetAvailableTaskPriorities {

    override suspend fun invoke(effect: Effect.GetAvailableTaskPriorities): OnTaskPrioritiesFound =
        withContext(coroutinesContextProvider.computation) {
            val priorities = TaskPriority.values().sortedWith { first, second ->
                first.compareTo(second)
            }

            if (effect.selectedPriority == null) {
                return@withContext OnTaskPrioritiesFound(priorities)
            }

            return@withContext OnTaskPrioritiesFound(
                priorities.toMutableList().apply {
                    val selectedPriorityIndex = priorities.indexOf(effect.selectedPriority)

                    if (selectedPriorityIndex != NotFound) {
                        removeAt(selectedPriorityIndex)
                    }
                    add(0, effect.selectedPriority)
                }
            )
        }
}

private const val NotFound = -1
