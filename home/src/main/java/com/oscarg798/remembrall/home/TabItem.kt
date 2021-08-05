package com.oscarg798.remembrall.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class TabItem(@StringRes val title: Int, var screen: @Composable () -> Unit)