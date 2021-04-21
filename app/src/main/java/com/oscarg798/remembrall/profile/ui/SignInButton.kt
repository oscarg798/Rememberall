package com.oscarg798.remembrall.profile.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.ui.getButtonTextStyle

@Composable
internal fun SignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.layoutId(SignInButtonId)
    ) {
        Text(
            text = stringResource(R.string.common_signin_button_text),
            style = getButtonTextStyle()
        )
    }
}
