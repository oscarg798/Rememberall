package com.oscarg798.remembrall.ui_common.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavControllerProvider = staticCompositionLocalOf<NavController> {
    error("NavController must be set first")
}
