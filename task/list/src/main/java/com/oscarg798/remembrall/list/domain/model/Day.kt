package com.oscarg798.remembrall.list.domain.model

data class Day(
    val name: String,
    val value: String,
    val isToday: Boolean = false
)
