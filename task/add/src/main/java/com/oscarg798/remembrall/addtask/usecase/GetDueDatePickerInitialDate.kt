package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import javax.inject.Inject

internal interface GetDueDatePickerInitialDate :
    suspend (Effect.GetDueDatePickerInitialDate) -> Event.OnDueDatePickerInitialDateFound

internal class GetDueDatePickerInitialDateImpl @Inject constructor(
    private val currentDateProvider: CurrentDateProvider
) : GetDueDatePickerInitialDate {

    override suspend fun invoke(
        effect: Effect.GetDueDatePickerInitialDate
    ): Event.OnDueDatePickerInitialDateFound = Event.OnDueDatePickerInitialDateFound(
        if(effect.dueDate!=null){
            effect.dueDate.date
        }else{
            currentDateProvider()
        }
    )
}