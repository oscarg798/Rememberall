package com.oscarg798.remembrall.widgetimpl.usecase

import java.time.LocalDateTime
import javax.inject.Inject

internal interface LocalDateTimeProvider {

    fun provide(): LocalDateTime
}

internal class CalendarProviderImpl @Inject constructor() : LocalDateTimeProvider {

    override fun provide(): LocalDateTime = LocalDateTime.now()
}