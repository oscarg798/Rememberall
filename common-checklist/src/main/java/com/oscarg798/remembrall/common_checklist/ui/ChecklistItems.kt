package com.oscarg798.remembrall.common_checklist.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import com.oscarg798.remembrall.ui_common.ui.draggedItem
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import org.burnoutcrew.reorderable.ItemPosition
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ChecklistItems(
    checklistItems: MutableList<ChecklistItem>,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onItemStatusChange: (ChecklistItem, Boolean) -> Unit,
    onMove: (fromPos: ItemPosition, toPos: ItemPosition) -> Unit,
    onRemoveClicked: (ChecklistItem) -> Unit,
) {
    val reorderState = rememberReorderState()

    LazyColumn(
        state = reorderState.listState,
        modifier = modifier
            .padding(vertical = RemembrallTheme.dimens.Small)
            .reorderable(
                state = reorderState,
                onMove = onMove
            )
            .fillMaxSize()
    ) {


        items(checklistItems, key = {
            it.id
        }) { checklistItem ->
            RemembrallCheckbox(
                checkListItem = checklistItem,
                enabled = !loading,
                modifier = Modifier
                    .padding(vertical = RemembrallTheme.dimens.Small)
                    .fillMaxWidth()
                    .draggedItem(reorderState.offsetByKey(checklistItem.id))
                    .detectReorderAfterLongPress(reorderState),
                onRemoveClicked = {
                    onRemoveClicked(checklistItem)
                }, onItemStatusChange = {
                    onItemStatusChange(checklistItem, it)
                }
            )
        }
    }
}