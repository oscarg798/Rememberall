package com.oscarg798.remembrall.ui

data class AwesomeIcon(
    // @SerializedName("name")
    val name: String,
    // @SerializedName("id")
    val id: String,
    // @SerializedName("unicode")
    val code: String,
    // @SerializedName("categories")
    val category: List<String>
)
