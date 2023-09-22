package com.oscarg798.remembrall.oauthimpl

import javax.inject.Inject
import java.util.Date

internal interface DateProvider {

    fun provideCurrentTimeInMillis(): Long
}

internal class DateProviderImpl @Inject constructor(): DateProvider {

    override fun provideCurrentTimeInMillis(): Long  = Date().time
}