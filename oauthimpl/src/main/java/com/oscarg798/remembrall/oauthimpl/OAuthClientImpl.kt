package com.oscarg798.remembrall.oauthimpl

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.OAuthClient
import com.oscarg798.remembrall.OAuthResponse
import com.oscarg798.remembrall.config.Config
import com.oscarg798.remembrall.oauthimpl.network.OAuthEndPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Field

internal class OAuthClientImpl @Inject constructor(
    private val config: Config,
    private val dateProvider: DateProvider,
    private val oAuthEndPoint: OAuthEndPoint,
    private val preferences: SharedPreferences,
    private val coroutineContextProvider: CoroutineContextProvider,
) : OAuthClient {

    private val serializer by lazy { Gson() }

    override suspend fun auth(authCode: String): OAuthResponse {
        val currentResponse = getCurrentOAuthResponse()

        if (currentResponse != null && !isTokenStillValid(currentResponse)) {
            return currentResponse
        }

        if (currentResponse?.refreshToken != null) {
            return refreshCurrentToken(currentResponse)
        }

        val response = oAuthEndPoint.auth(buildMap {
            put("code", authCode)
            put("client_id", config.clientId)
            put("redirect_uri", RedirectUri)
            put("grant_type", AuthGrantType)
            put("client_secret", config.clientSecret)
        })

        val oAuthResponse = OAuthResponse(
            accessToken = response.accessToken,
            tokenType = response.tokenType,
            expiresIn = response.expiresIn,
            refreshToken = response.refreshToken,
            idToken = response.idToken,
            issuedOn = dateProvider.provideCurrentTimeInMillis()
        )

        saveOAuthResponse(oAuthResponse)

        return oAuthResponse
    }

    private suspend fun refreshCurrentToken(currentResponse: OAuthResponse): OAuthResponse {
        val refreshResponse = oAuthEndPoint.auth(buildMap {
            put("client_id", config.clientId)
            put("grant_type", RefreshGrantType)
            put("client_secret", config.clientSecret)
            put("refresh_token", currentResponse.refreshToken!!)
        })

        val updatedOAuthResponse = currentResponse.copy(
            accessToken = refreshResponse.accessToken,
            expiresIn = refreshResponse.expiresIn,
        )
        saveOAuthResponse(updatedOAuthResponse)
        return updatedOAuthResponse
    }

    private suspend fun getCurrentOAuthResponse(): OAuthResponse? =
        withContext(coroutineContextProvider.io) {
            val savedResponse =
                preferences.getString(OAuthResponseKey, null) ?: return@withContext null

            serializer.fromJson(savedResponse, OAuthResponse::class.java)
        }

    private suspend fun saveOAuthResponse(oAuthResponse: OAuthResponse) =
        withContext(coroutineContextProvider.io) {
            preferences.edit().putString(OAuthResponseKey, serializer.toJson(oAuthResponse))
                .commit()
        }

    private fun isTokenStillValid(oAuthResponse: OAuthResponse): Boolean {
        val currentTime = dateProvider.provideCurrentTimeInMillis()
        //expires is given in seconds one second is 1000 millis
        val tokenAge = (currentTime - oAuthResponse.issuedOn) / MillisUnit

        return tokenAge < oAuthResponse.expiresIn
    }
}

private const val MillisUnit = 1_000
private const val RedirectUri = "https://parking-5ec2f.web.app/"
private const val AuthGrantType = "authorization_code"
private const val RefreshGrantType = "refresh_token"
private const val OAuthResponseKey = "OAuthResponse"