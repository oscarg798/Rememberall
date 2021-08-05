package com.oscarg798.remembrall.ui_common.ui

import com.google.gson.annotations.SerializedName

data class AwesomeIcon(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("unicode")
    val code: String,
    @SerializedName("categories")
    val category: List<String>
)