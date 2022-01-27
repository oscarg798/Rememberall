package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common_task.ui.TaskBody
import com.oscarg798.remembrall.common_task.ui.TaskCard
import com.oscarg798.remembrall.common_task.ui.TaskCardOptions

@Composable
internal fun TaskItem(
    task: DisplayableTask,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    options: List<TaskCardOptions.Option>,
    onOptionClicked: (DisplayableTask, TaskCardOptions.Option) -> Unit
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
            taskCardOptions = TaskCardOptions.Present(
                options
            ),
            onOptionClicked =  onOptionClicked
        )
    }
}
