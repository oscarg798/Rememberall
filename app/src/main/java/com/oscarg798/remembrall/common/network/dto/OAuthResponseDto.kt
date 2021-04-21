package com.oscarg798.remembrall.common.network.dto

import com.google.gson.annotations.SerializedName

data class OAuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String
)
