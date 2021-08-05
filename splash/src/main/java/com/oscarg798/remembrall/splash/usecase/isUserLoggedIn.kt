package com.oscarg798.remembrall.splash.usecase

import com.oscarg798.remembrall.common.auth.AuthRepository
import javax.inject.Inject

class isUserLoggedIn @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke() = authRepository.isUserLoggedIn()
}
