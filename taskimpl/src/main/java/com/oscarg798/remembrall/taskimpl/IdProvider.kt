package com.oscarg798.remembrall.taskimpl

import java.util.UUID
import javax.inject.Inject

internal interface IdProvider : () -> String

internal class UUIDIdProvider @Inject constructor() : IdProvider {

    override fun invoke(): String = UUID.randomUUID().toString()
}
