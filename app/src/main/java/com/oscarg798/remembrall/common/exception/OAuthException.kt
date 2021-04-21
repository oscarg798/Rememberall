package com.oscarg798.remembrall.common.exception

import java.io.IOException

sealed class OAuthException(override val message: String) : IOException() {

    class ClientAuthMissing :
        OAuthException("Client auth is missing, is user authenticated?")

    class ServerAuthError(accessToken: String) :
        OAuthException("Error authenticating against server with auth code: $accessToken")
}
