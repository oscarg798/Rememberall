package com.oscarg798.remembrall.addtask.usecase

import java.time.LocalDateTime
import javax.inject.Inject

internal interface CurrentDateProvider : () -> LocalDateTime

internal class CurrentDateProviderImpl @Inject constructor() : CurrentDateProvider {

    override fun invoke(): LocalDateTime {
        return LocalDateTime.now()
    }
}
