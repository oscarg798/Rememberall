package com.oscarg798.remembrall.tasklist.model

data class Day(
    val name: String,
    val value: String,
    val isToday: Boolean = false
)
