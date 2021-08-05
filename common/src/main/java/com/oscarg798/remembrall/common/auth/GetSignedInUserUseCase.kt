package com.oscarg798.remembrall.common.auth

import com.oscarg798.remembrall.common.model.User
import javax.inject.Inject

class GetSignedInUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    fun execute(): User = authRepository.getSignedInUser()
}
