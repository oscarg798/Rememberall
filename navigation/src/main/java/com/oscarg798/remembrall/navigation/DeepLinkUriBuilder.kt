package com.oscarg798.remembrall.navigation

import android.net.Uri

interface DeepLinkUriBuilder {

    fun appendPath(path: String): Uri
}