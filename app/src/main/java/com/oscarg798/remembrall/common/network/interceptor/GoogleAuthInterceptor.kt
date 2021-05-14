package com.oscarg798.remembrall.common.network.interceptor

import arrow.core.getOrHandle
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.oscarg798.remembrall.common.exception.OAuthException
import com.oscarg798.remembrall.common.network.dto.OAuthResponseDto
import com.oscarg798.remembrall.common.network.restclient.ExternalSignInClient
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class GoogleAuthInterceptor @Inject constructor(
    private val gson: Gson,
    private val externalSignInClient: ExternalSignInClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val clientToken = runBlocking {
            externalSignInClient.silentSignIn().getOrHandle { error ->
                throw error
            }.serverAuthToken
        } ?: throw OAuthException.ClientAuthMissing()

        val authRequest = chain.request().newBuilder().url(AuthUrl)
            .post(
                gson.toJson(AuthRequestBody(clientToken))
                    .toRequestBody(AuthRequestMediaType.toMediaType())
            ).build()

        val authResponse = chain.proceed(authRequest)

        if (!authResponse.isSuccessful) {
            throw OAuthException.ServerAuthError(clientToken)
        }

        val accessToken =
            gson.fromJson(authResponse.body!!.string(), OAuthResponseDto::class.java).accessToken

        val response = chain.proceed(
            originalRequest.newBuilder().url(
                originalRequest.url.newBuilder()
                    .addEncodedQueryParameter(AuthQueryParamName, accessToken).build()
            ).build()
        )

        return response
    }

    private class AuthRequestBody(@SerializedName("accessToken") val accessToken: String)
}

private const val AuthQueryParamName = "access_token"
private const val AuthRequestMediaType = "application/json; charset=utf-8"
private const val AuthUrl = "https://us-central1-parking-5ec2f.cloudfunctions.net/auth"
