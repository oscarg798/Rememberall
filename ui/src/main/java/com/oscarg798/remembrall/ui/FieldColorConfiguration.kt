package com.oscarg798.remembrall.ui

import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RemembrallTextFielColorConfiguration() = TextFieldDefaults.textFieldColors(
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    placeholderColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary,
    backgroundColor = MaterialTheme.colorScheme.background,
    textColor = MaterialTheme.colorScheme.onBackground,
    unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
)
