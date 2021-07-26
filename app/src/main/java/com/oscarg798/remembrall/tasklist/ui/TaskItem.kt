package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.ui.TaskBody
import com.oscarg798.remembrall.common.ui.TaskCard
import com.oscarg798.remembrall.common.ui.TaskCardOptions
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun TaskItem(
    task: DisplayableTask,
    onClick: (String) -> Unit,
    onRemoveClicked: () -> Unit
) {

    TaskCard(
        task = task,
        onClick = {
            onClick(task.id)
        },
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                vertical = Dimensions.Spacing.Small,
                horizontal = Dimensions.Spacing.Medium
            )
    ) {
        TaskBody(
            task = task,
            taskCardOptions = TaskCardOptions.Present(onRemoveClicked = onRemoveClicked)
        )
    }
}
