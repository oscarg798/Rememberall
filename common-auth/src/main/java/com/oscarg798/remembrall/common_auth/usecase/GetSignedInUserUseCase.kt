package com.oscarg798.remembrall.common_auth.usecase

import com.oscarg798.remembrall.common_auth.model.User
import com.oscarg798.remembrall.common_auth.repository.domain.AuthRepository
import javax.inject.Inject

class GetSignedInUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    fun execute(): User = authRepository.getSignedInUser()
}
