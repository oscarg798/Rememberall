package com.oscarg798.remembrall.homeutils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

interface HomeContent {

    @Composable
    fun Content(backStack: NavBackStackEntry)
}
