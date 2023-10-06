package com.oscarg798.remembrall.navigationimpl

import android.net.Uri
import androidx.core.net.toUri
import com.oscarg798.remembrall.navigation.DeepLinkUriBuilder
import com.oscarg798.remembrall.navigation.Navigator
import javax.inject.Inject

internal class DeepLinkUriBuilderImpl @Inject constructor() : DeepLinkUriBuilder {

    private val deepLinkUri = Navigator.DeepLinkUri.toUri()

    override fun appendPath(path: String): Uri {
        return Uri.Builder()
            .scheme(deepLinkUri.scheme)
            .authority(deepLinkUri.authority)
            .path(path)
            .build()
    }
}