package com.oscarg798.remembrall.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink

interface Route {

    val route: String
    val uriPattern: Uri


}

