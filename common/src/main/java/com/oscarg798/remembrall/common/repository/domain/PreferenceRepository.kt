package com.oscarg798.remembrall.common.repository.domain

interface PreferenceRepository {

    fun saveNotificationValue(notificationsEnabled: Boolean)

    fun getNotificationValue(): Boolean
}
