package com.oscarg798.remembrall.calendarimpl.data.interceptor

import com.oscarg798.remembrall.OAuthClient
import com.oscarg798.remembrall.auth.Session
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class GoogleAuthInterceptor @Inject constructor(
    private val session: Session,
    private val oAuthClient: OAuthClient,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val sessionState = runBlocking { session.getLoggedInState() }

        val accessCode = if (sessionState is Session.State.LoggedIn &&
            sessionState.user.serverAuthToken != null
        ) {
            sessionState.user.serverAuthToken
        } else {
            runBlocking { session.silentLoginIng().user }.serverAuthToken
        } ?: throw NullPointerException("Can not get server auth code ")

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