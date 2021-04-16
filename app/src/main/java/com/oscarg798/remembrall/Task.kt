package com.oscarg798.remembrall

data class Task(
    private val id: String,
    private val name: String,
    private val description: String,
    private val priority: TaskPriority,
    private val due: Long
)