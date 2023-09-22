package com.oscarg798.remembrall.common.auth

import com.oscarg798.remembrall.user.User
import javax.inject.Inject

class GetSignedInUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(): User = authRepository.getSignedInUser()
}
