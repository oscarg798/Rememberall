package com.oscarg798.remembrall

import com.oscarg798.remembrall.addtask.ui.AddTaskScreenConfiguration
import com.oscarg798.remembrall.addtask.usecase.GetAddTaskConfiguration
import com.oscarg798.remembrall.common.model.TaskPriority
import org.junit.Test

class GetTaskConfigurationTest {

    @Test
    fun `when is executed then returns right task add task configutation`() {
        assert(
            AddTaskScreenConfiguration(
                availablePriorities = TaskPriority.values().sortedWith { first, second ->
                    first.compareTo(second)
                }.reversed(),
                selectedPriority = TaskPriority.Low
            ) == GetAddTaskConfiguration().execute()
        )
    }
}
