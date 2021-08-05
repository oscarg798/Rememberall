package com.oscarg798.remembrall.common_checklist.model

data class Checklist(
    val id: String,
    val name: String,
    val items: List<ChecklistItem>,
    val icon: String,
    val owner: String
)