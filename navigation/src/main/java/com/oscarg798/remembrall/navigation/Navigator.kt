package com.oscarg798.remembrall.navigation

import android.os.Bundle

interface Navigator {

    //TODO: Add navigation options here such as singleTop
    fun navigate(route: Route, arguments: Bundle? = null)

    fun navigateBack()

    companion object {
        const val DeepLinkUri = "https://remembrall"
    }
}

