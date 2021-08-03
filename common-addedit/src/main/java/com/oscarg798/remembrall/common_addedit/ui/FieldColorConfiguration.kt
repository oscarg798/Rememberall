package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
internal fun addTaskFieldColorConfiguration() = TextFieldDefaults.textFieldColors(
    focusedLabelColor = MaterialTheme.colors.secondary,
    placeholderColor = MaterialTheme.colors.secondary,
    cursorColor = MaterialTheme.colors.secondary,
    backgroundColor = MaterialTheme.colors.background,
    textColor = MaterialTheme.colors.onBackground
)
