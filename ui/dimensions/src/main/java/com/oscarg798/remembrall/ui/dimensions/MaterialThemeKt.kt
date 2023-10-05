package com.oscarg798.remembrall.ui.dimensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

val MaterialTheme.dimensions
    @Composable
    get() = LocalAppDimens.current

@Deprecated("MaterialTheme from Material3 must be used")
val androidx.compose.material.MaterialTheme.dimensions
    @Composable
    get() = LocalAppDimens.current

@Deprecated("MaterialTheme3 typography object must be used")
val MaterialTheme.typo
    @Composable
    get() = androidx.compose.material.MaterialTheme.typography

@Deprecated("MaterialTheme from Material3 must be used")
val androidx.compose.material.MaterialTheme.colorScheme
    @Composable
    get() = MaterialTheme.colorScheme

object RemembrallTheme {

    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}