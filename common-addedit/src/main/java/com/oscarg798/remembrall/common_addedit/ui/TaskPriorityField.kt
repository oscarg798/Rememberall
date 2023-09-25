package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.oscarg798.remembrall.task.TaskPriority

@Composable
internal fun TaskPriorityField(
    availablePriorities: List<TaskPriority>,
    selectedPriority: TaskPriority? = null,
    enabled: Boolean,
    onChipClicked: (TaskPriority) -> Unit
) {

    LazyRow() {
        items(availablePriorities) { priority ->
            TaskPriorityChip(
                enabled = enabled,
                priority = priority,
                onChipClicked = onChipClicked,
                isSelected = priority == selectedPriority
            )
        }
    }
}
