package com.oscarg798.remembrall.common.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class PageConfigurator(
    val title: String? = null,
    val addButtonEnabled: Boolean = false,
    val onAddButtonClicked: () -> Unit = {},
    val pageBackgroundColor: Color,
    val topBarBackgroundColor: Color,
    val toolbarRightButton: (@Composable () -> Unit)? = null,
) {

    companion object {

        @Composable
        fun buildPageConfigurator() =
            PageConfigurator(
                topBarBackgroundColor = MaterialTheme.colors.secondary,
                pageBackgroundColor = MaterialTheme.colors.background
            )
    }
}
