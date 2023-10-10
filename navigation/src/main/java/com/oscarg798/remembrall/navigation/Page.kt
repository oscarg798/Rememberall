package com.oscarg798.remembrall.navigation

import androidx.navigation.NavGraphBuilder

/**
 * Represents a NavHost composable
 * please provide it within [ActivityRetainedComponent] with [IntoSet]
 */
fun interface Page {
    fun build(builder: NavGraphBuilder)
}