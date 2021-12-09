package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.ui_common.ui.TaskBody
import com.oscarg798.remembrall.ui_common.ui.TaskCard
import com.oscarg798.remembrall.ui_common.ui.TaskCardOptions

@Composable
internal fun TaskItem(
    task: DisplayableTask,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onRemoveClicked: (DisplayableTask) -> Unit
) {

    TaskCard(
        task = task,
        onClick = {
            onClick(task.id)
        },
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        TaskBody(
            task = task,
            taskCardOptions = TaskCardOptions.Present(onRemoveClicked = onRemoveClicked)
        )
    }
}
