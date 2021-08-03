package com.oscarg798.remebrall.splash.usecase

import com.oscarg798.remembrall.common_auth.repository.domain.AuthRepository
import javax.inject.Inject

class VerifyUserSessionUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute() {
        authRepository.silentAuth()
    }
}
