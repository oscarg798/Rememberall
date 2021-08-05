package com.oscarg798.remembrall.common.extensions

import androidx.navigation.NavBackStackEntry

fun NavBackStackEntry.requireArguments() = arguments ?: error("Arguments must not be null")
