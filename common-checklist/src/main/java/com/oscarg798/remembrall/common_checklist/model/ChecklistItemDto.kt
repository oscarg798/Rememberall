package com.oscarg798.remembrall.common_checklist.model

data class ChecklistItemDto(
    val id: String,
    val name: String,
    val completed: Boolean,
    val position: Int
) {

    constructor(checklistItem: ChecklistItem) : this(
        id = checklistItem.id,
        name = checklistItem.name,
        completed = checklistItem.completed,
        position = checklistItem.position
    )

    constructor(id: String, map: Map<String, Any>) : this(
        id = id,
        name = map[ColumnNames.Name] as? String
            ?: throw IllegalStateException("Checklist item $id must have name"),
        completed = map[ColumnNames.Completed] as? Boolean
            ?: throw IllegalStateException("Checklist item $id must  have Completed field"),
        position = (map[ColumnNames.Position] as? Long)?.toInt()
            ?: throw IllegalStateException("Checklist item $id must have a Position")
    )

    fun toMap(): Map<String, Any?> = hashMapOf(
        ColumnNames.Name to name,
        ColumnNames.Completed to completed,
        ColumnNames.Position to position
    )

    fun toChecklistItem(): ChecklistItem = ChecklistItem(
        id = id,
        name = name,
        completed = completed,
        position = position
    )

    private object ColumnNames {
        const val Name = "name"
        const val Completed = "completed"
        const val Position = "position"
    }
}