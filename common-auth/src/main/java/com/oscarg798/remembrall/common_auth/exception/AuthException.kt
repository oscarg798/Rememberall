package com.oscarg798.remembrall.common_auth.exception

import okio.IOException

sealed class AuthException : IOException() {

    class AuthRequired(override val cause: Exception? = null) : AuthException()
    class LogOutError(override val cause: Exception?) : AuthException()
}
