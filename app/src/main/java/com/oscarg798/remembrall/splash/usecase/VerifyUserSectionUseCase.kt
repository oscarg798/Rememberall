package com.oscarg798.remembrall.splash.usecase

import com.oscarg798.remembrall.common_auth.repository.domain.AuthRepository
import javax.inject.Inject

class VerifyUserSectionUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute() {
        authRepository.silentAuth()
    }
}
