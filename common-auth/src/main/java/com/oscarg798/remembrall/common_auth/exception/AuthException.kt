package com.oscarg798.remembrall.common_auth.exception

import okio.IOException

sealed class AuthException(
    message: String? = null,
    cause: Exception? = null
) : IOException(message, cause) {

    class LoggedUserNotFound : AuthException()
    class AuthRequired(override val cause: Exception? = null) : AuthException(cause = cause)
    class LogOutError(override val cause: Exception?) : AuthException(cause = cause)
    class DataIntegrityError(field: String) : AuthException(message = "User must $field  field")
    class ErrorFinishingLogOutProcessWithExternalAuthenticators(override val cause: Exception?) : AuthException(cause = cause)
}
