package com.oscarg798.remembrall.common.provider

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    StringProvider {

    override fun get(id: Int): String = context.getString(id)
}
