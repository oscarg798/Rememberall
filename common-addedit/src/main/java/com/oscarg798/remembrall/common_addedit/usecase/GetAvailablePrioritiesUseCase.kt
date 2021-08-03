package com.oscarg798.remembrall.common_addedit.usecase

import com.oscarg798.remembrall.common.model.TaskPriority
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetAvailablePrioritiesUseCase @Inject constructor() {

    fun execute(): List<TaskPriority> = TaskPriority.values().sortedWith { first, second ->
        first.compareTo(second)
    }.reversed()
}
