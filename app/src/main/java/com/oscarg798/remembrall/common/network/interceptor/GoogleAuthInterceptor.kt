package com.oscarg798.remembrall.common.network.interceptor

import arrow.core.getOrElse
import arrow.core.getOrHandle
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.oscarg798.remembrall.OAuthClient
import com.oscarg798.remembrall.common.exception.OAuthException
import com.oscarg798.remembrall.common.network.dto.OAuthResponseDto
import com.oscarg798.remembrall.common_auth.network.restclient.ExternalSignInClient
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class GoogleAuthInterceptor @Inject constructor(
    private val oAuthClient: OAuthClient,
    private val externalSignInClient: ExternalSignInClient,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val signedUser = runBlocking {
            externalSignInClient.getSignedInUser().getOrElse { null }
        }
        val accessCode = if (signedUser?.serverAuthToken != null) {
            signedUser.serverAuthToken!!
        } else {
            runBlocking {
                externalSignInClient.silentSignIn().getOrHandle { error ->
                    throw error
                }.serverAuthToken
            } ?: throw OAuthException.ClientAuthMissing()
        }

        val accessToken = runBlocking {
            oAuthClient.auth(accessCode).accessToken
        }

        val interceptedRequest = originalRequest.newBuilder().url(
            originalRequest.url.newBuilder()
                .addEncodedQueryParameter(AuthQueryParamName, accessToken)
                .build()
        ).build()

        return chain.proceed(interceptedRequest)
    }
}

private const val AuthQueryParamName = "access_token"