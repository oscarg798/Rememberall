package com.oscarg798.remembrall.addtask.usecase


import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.dateformatter.DueDateFormatter
import javax.inject.Inject
import java.time.LocalDateTime

internal interface FormatDueDate : suspend (Effect.FormatDueDate) -> Event.OnDueDateFormatted

internal class FormatDueDateImpl @Inject constructor(
    private val dueDateFormatter: DueDateFormatter
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
