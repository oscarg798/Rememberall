package com.oscarg798.remembrall.common.repository.data

import android.content.SharedPreferences
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import javax.inject.Inject

class LocalPreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) :
    PreferenceRepository {

    override fun saveNotificationValue(notificationsEnabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(NotificationsKey, notificationsEnabled)
            .apply()
    }

    override fun getNotificationValue(): Boolean =
        sharedPreferences.getBoolean(NotificationsKey, false)
}

private const val NotificationsKey = "NotificationKey"
