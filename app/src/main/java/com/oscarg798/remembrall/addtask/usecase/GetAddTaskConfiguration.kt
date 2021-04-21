package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.ui.AddTaskScreenConfiguration
import com.oscarg798.remembrall.common.model.TaskPriority
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetAddTaskConfiguration @Inject constructor() {

    fun execute(): AddTaskScreenConfiguration =
        AddTaskScreenConfiguration(
            availablePriorities = TaskPriority.values().sortedWith { first, second ->
                first.compareTo(second)
            }.reversed(),
            selectedPriority = TaskPriority.Low
        )
}
