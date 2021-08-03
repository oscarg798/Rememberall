package com.oscarg798.remembrall.common_auth.exception

sealed class AuthException : RuntimeException() {

    class AuthRequired(override val cause: Exception? = null) : AuthException()
    class LogOutError(override val cause: Exception?) : AuthException()
}
