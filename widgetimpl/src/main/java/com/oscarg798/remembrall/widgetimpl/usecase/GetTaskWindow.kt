package com.oscarg798.remembrall.widgetimpl.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.widgetimpl.domain.TaskWindow
import javax.inject.Inject

internal interface GetTaskWindow : () -> TaskWindow

internal class GetTaskWindowImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val localDateTimeProvider: LocalDateTimeProvider
) : GetTaskWindow {

    override fun invoke(): TaskWindow {
        val now = localDateTimeProvider.provide()
            .withHour(0)
            .withMinute(0)

        val start = dateFormatter.toMillis(now.minusDays(TaskWindow))
        val end = dateFormatter.toMillis(now.plusDays(TaskWindow))

        return TaskWindow(
            start = start,
            end = end,
            startFormatted = dateFormatter.toDisplayableDate(start, true),
            endFormatted = dateFormatter.toDisplayableDate(end, true)
        )
    }
}

private const val TaskWindow = 2L