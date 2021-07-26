package com.oscarg798.remembrall.common.usecase

import com.oscarg798.remembrall.common.model.User
import com.oscarg798.remembrall.common.repository.domain.AuthRepository
import javax.inject.Inject

class GetSignedInUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    fun execute(): User = authRepository.getSignedInUser()
}
