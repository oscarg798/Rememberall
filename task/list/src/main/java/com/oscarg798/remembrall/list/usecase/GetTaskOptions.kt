package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.ui.TaskCardOptions
import javax.inject.Inject

internal interface GetTaskOptions : (Effect.GetTaskOptions) -> Event.OnTaskOptionsFound

internal class GetTaskOptionsImpl @Inject constructor() : GetTaskOptions {

    override fun invoke(effect: Effect.GetTaskOptions): Event.OnTaskOptionsFound {
        return Event.OnTaskOptionsFound(
            if (effect.task.owned) {
                listOf(TaskCardOptions.Option.Edit, TaskCardOptions.Option.Remove)
            } else {
                emptyList()
            }
        )
    }
}