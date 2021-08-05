package com.oscarg798.remembrall.common_checklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.RelocationRequester
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.imePadding
import com.oscarg798.remembrall.common_checklist.R

@Composable
fun RemembrallAddCheckboxTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onSubmit: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val relocationRequestor = remember { RelocationRequester() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painterResource(id = R.drawable.ic_checkbox),
            modifier = Modifier
                .weight(.1f)
                .height(25.dp),
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground
        )

        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier
                .relocationRequester(relocationRequestor)
                .weight(.9f),
            singleLine = true,
            enabled = enabled,
            keyboardActions = KeyboardActions(onDone = {
                onSubmit(value)
                keyboardController?.hide()
            })
        )
    }
}