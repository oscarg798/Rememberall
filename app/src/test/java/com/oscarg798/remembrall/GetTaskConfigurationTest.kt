package com.oscarg798.remembrall

import com.oscarg798.remembrall.addtask.ui.AddtaskScreenConfiguration
import com.oscarg798.remembrall.task.TaskPriority
import org.junit.Test

class GetTaskConfigurationTest {

    @Test
    fun `when is executed then returns right task add task configutation`() {
        assert(
            AddtaskScreenConfiguration(
                availablePriorities = TaskPriority.values().sortedWith { first, second ->
                    first.compareTo(second)
                }.reversed(),
                selectedPriority = TaskPriority.Low
            ) == GetAvailablePrioritiesUseCase().execute()
        )
    }
}
