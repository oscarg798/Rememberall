package com.oscarg798.remembrall.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalNavigatorProvider = staticCompositionLocalOf<Navigator> {
    error("A Navigator must be provided before accessing it")
}