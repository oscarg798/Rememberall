package com.oscarg798.remembrall.oauthimpl

import java.util.Date
import javax.inject.Inject

internal interface DateProvider {

    fun provideCurrentTimeInMillis(): Long
}

internal class DateProviderImpl @Inject constructor() : DateProvider {

    override fun provideCurrentTimeInMillis(): Long = Date().time
}
