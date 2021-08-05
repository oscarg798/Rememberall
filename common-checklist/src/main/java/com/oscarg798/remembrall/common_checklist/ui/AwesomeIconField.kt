package com.oscarg798.remembrall.common_checklist.ui

import android.text.Html
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.oscarg798.remembrall.ui_common.ui.AwesomeIcon
import com.oscarg798.remembrall.ui_common.ui.theming.LocalFontAwesome


@Composable
fun AwesomeIconField(iconCode: String, modifier: Modifier) {
    Text(
        text = Html.fromHtml(iconCode).toString(),
        style = MaterialTheme.typography.h3.merge(
            TextStyle(
                fontFamily = LocalFontAwesome.current,
                fontWeight = FontWeight.Normal,
            )
        ),
        modifier = modifier
    )
}
