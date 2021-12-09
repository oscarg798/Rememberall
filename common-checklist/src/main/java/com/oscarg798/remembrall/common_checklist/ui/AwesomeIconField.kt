package com.oscarg798.remembrall.common_checklist.ui

import android.text.Html
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.oscarg798.remembrall.ui_common.ui.theming.LocalFontAwesome


@Composable
fun AwesomeIconField(iconCode: String, modifier: Modifier) {
    Text(
        text = Html.fromHtml(iconCode).toString(),
        style = MaterialTheme.typography.bodyLarge.merge(
            TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = LocalFontAwesome.current,
                fontWeight = FontWeight.Normal,
            )
        ),
        modifier = modifier
    )
}
