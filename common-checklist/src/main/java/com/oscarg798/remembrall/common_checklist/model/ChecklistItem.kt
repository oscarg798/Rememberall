package com.oscarg798.remembrall.common_checklist.model

data class ChecklistItem(
    val id: String,
    val name: String,
    val completed: Boolean,
    val position: Int
)