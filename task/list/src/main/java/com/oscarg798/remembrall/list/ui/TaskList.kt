package com.oscarg798.remembrall.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.list.domain.model.DisplayableTask
import com.oscarg798.remembrall.list.domain.model.DisplayableTasksGroup
import com.oscarg798.remembrall.list.domain.model.TaskGroup
import com.oscarg798.remembrall.ui.AddButton
import com.oscarg798.remembrall.ui.dimensions.dimensions

@Composable
internal fun TaskList(
    tasks: Map<TaskGroup.MonthGroup, DisplayableTasksGroup>,
    modifier: Modifier,
    initialIndex: Int = -1,
    options: List<TaskCardOptions.Option>,
    onAddButtonClicked: () -> Unit,
    onClick: (String) -> Unit,
    onOptionClicked: (DisplayableTask, TaskCardOptions.Option) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = initialIndex) {
        if (tasks.isEmpty() || initialIndex == -1) return@LaunchedEffect
        listState.scrollToItem(initialIndex)
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = MaterialTheme.dimensions.Medium)
                .fillMaxSize()
        ) {
            tasks.entries.forEach { entry ->
                item(key = entry.key.toString()) {
                    Text(
                        text = entry.key.toString(),
                        style = MaterialTheme.typography.displaySmall
                            .merge(TextStyle(color = MaterialTheme.colorScheme.onBackground)),
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.dimensions.Large)
                            .fillMaxWidth()
                    )
                }

                val groups = entry.value
                items(groups.itemsByDay.keys.toList(), key = {
                    "${it.dayName}_${it.dayNumber}_${it.year}"
                }) { dayGroup ->
                    Row(
                        Modifier.padding(
                            vertical = MaterialTheme.dimensions.Small,
                            horizontal = MaterialTheme.dimensions.Medium
                        )
                    ) {
                        DayGroupField(
                            dayGroup = dayGroup,
                            modifier = Modifier.padding(
                                top = MaterialTheme.dimensions.Medium,
                                start = MaterialTheme.dimensions.Small
                            )
                        )
                        Column(
                            modifier = Modifier.padding(
                                start = MaterialTheme.dimensions.Small,
                            )
                        ) {
                            groups.itemsByDay[dayGroup]?.map {
                                TaskItem(
                                    task = it,
                                    onClick = { taskId ->
                                        onClick(taskId)
                                    },
                                    options = options,
                                    onOptionClicked = onOptionClicked,
                                    modifier = Modifier.padding(
                                        vertical = MaterialTheme.dimensions.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.dimensions.Medium)
        ) {
            onAddButtonClicked()
        }
    }
}

@Composable
private fun DayGroupField(dayGroup: TaskGroup.DayGroup, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = dayGroup.dayName,
            style = MaterialTheme.typography.bodyLarge.merge(
                TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        )
        Text(
            text = dayGroup.dayNumber,
            style = MaterialTheme.typography.bodyLarge.merge(
                TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        )
    }
}
