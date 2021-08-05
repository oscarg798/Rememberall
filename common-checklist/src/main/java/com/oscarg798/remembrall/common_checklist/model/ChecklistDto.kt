package com.oscarg798.remembrall.common_checklist.model

class ChecklistDto(
    val id: String,
    val name: String,
    val items: List<ChecklistItemDto>,
    val icon: String,
    val owner: String
) {

    constructor(checklist: Checklist) : this(
        id = checklist.id,
        name = checklist.name,
        items = checklist.items.map {
            ChecklistItemDto(it)
        },
        icon = checklist.icon,
        owner = checklist.owner
    )

    constructor(id: String, map: Map<String, Any>, checklistItems: List<ChecklistItemDto>) : this(
        id = id,
        name = map[ColumnNames.Name] as? String
            ?: throw IllegalStateException("Checklist item must have name"),
        items = checklistItems,
        icon = map[ColumnNames.Icon] as? String
            ?: throw IllegalStateException("Checklist must include an Icon"),
        owner = map[ColumnNames.Owner] as? String
            ?: throw IllegalStateException("Checklist item must have an owner")
    )

    fun toMap(): Map<String, Any?> = hashMapOf(
        ColumnNames.Id to id,
        ColumnNames.Name to name,
        ColumnNames.Owner to owner,
        ColumnNames.Icon to icon
    )

    fun toChecklist(): Checklist = Checklist(
        id = id,
        name = name,
        items = items.map { it.toChecklistItem() },
        icon = icon,
        owner = owner
    )

    object ColumnNames {
        const val Id = "id"
        const val Name = "name"
        const val Items = "items"

        const val Icon = "icon"
        const val Owner = "owner"
    }
}