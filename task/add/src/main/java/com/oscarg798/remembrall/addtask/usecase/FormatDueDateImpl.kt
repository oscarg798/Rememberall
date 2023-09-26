package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.dateformatter.DateFormatter
import java.time.LocalDateTime
import javax.inject.Inject

internal interface FormatDueDate : suspend (Effect.FormatDueDate) -> Event.OnDueDateFormatted

internal class FormatDueDateImpl @Inject constructor(
    private val dueDateFormatter: DateFormatter
) : FormatDueDate {

    override suspend fun invoke(effect: Effect.FormatDueDate): Event.OnDueDateFormatted {
        val selectedDate = LocalDateTime.of(effect.date, effect.time)

        return Event.OnDueDateFormatted(
            date = selectedDate,
            formattedDate = dueDateFormatter.toDisplayableDate(date = selectedDate, short = true)
        )
    }

    fun execute(dueDate: LocalDateTime): String = dueDateFormatter.toDisplayableDate(dueDate)
}
