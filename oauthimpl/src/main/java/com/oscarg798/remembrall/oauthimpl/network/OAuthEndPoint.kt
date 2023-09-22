package com.oscarg798.remembrall.oauthimpl.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal interface OAuthEndPoint {

    @FormUrlEncoded
    @POST("oauth2/v4/token")
    suspend fun auth(
       @FieldMap formValues: Map<String, String>
    ): AuthResponse

    data class AuthResponse(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("scope")
        val scope: String,
        @SerializedName("token_type")
        val tokenType: String,
        @SerializedName("expires_in")
        val expiresIn: Long,
        @SerializedName("refresh_token")
        val refreshToken: String?,
        @SerializedName("id_token")
        val idToken: String?
    )
}