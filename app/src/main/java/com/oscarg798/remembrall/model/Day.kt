package com.oscarg798.remembrall.model

data class Day(
    val name: String,
    val value: String,
    val isToday: Boolean = false
)