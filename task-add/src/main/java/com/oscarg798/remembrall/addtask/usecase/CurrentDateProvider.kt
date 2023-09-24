package com.oscarg798.remembrall.addtask.usecase

import javax.inject.Inject
import java.time.LocalDateTime

internal interface CurrentDateProvider : () -> LocalDateTime

internal class CurrentDateProviderImpl @Inject constructor() : CurrentDateProvider {

    override fun invoke(): LocalDateTime {
        return LocalDateTime.now()
    }
}