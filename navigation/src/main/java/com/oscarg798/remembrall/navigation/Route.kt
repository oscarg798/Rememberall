package com.oscarg798.remembrall.navigation

import android.net.Uri
import androidx.core.net.toUri

enum class Route(val path: String) {

    ADD("task/add") {
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path/{$TaskIdArgument}".toUri()
        }
    },
    DETAIL("task/detail") {
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path/{$TaskIdArgument}".toUri()
        }
    },
    HOME("home"){
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path".toUri()
        }
    },
    LIST("task/list") {
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path".toUri()
        }
    },
    LOGIN("login") {
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path".toUri()
        }
    },
    PROFILE("profile") {
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path".toUri()
        }
    },
    SPLASH("splash") {
        override val uriPattern: Uri by lazy {
            "${Navigator.DeepLinkUri}/$path".toUri()
        }
    };


    abstract val uriPattern: Uri

    companion object {
        const val NO_PARAMETER = "none"
        const val TaskIdArgument = "TaskId"
    }

}

private const val AddTaskRoute = "addTask"

