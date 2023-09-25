package com.oscarg798.remembrall.taskimpl

import javax.inject.Inject
import java.util.UUID

internal interface IdProvider : () -> String

internal class UUIDIdProvider @Inject constructor(): IdProvider {

    override fun invoke(): String  = UUID.randomUUID().toString()
}