package com.oscarg798.remebrall.schedule.usecase

import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import javax.inject.Inject

class AreNotificationEnableUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {

    fun execute(): Boolean = preferenceRepository.getNotificationValue()
}
