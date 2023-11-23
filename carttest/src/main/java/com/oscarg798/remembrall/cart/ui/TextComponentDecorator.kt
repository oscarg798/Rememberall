package com.oscarg798.remembrall.cart.ui

import javax.inject.Inject
import com.oscarg798.remembrall.cart.R

internal interface PermissionChecker {

    fun mutate(hasPermissions: Boolean)

    fun hasPermissions(): Boolean
}

internal class PermissionCheckerImpl @Inject constructor() : PermissionChecker {

    private var hasPermissions = false

    override fun mutate(hasPermissions: Boolean) {
        this.hasPermissions = hasPermissions
    }

    override fun hasPermissions(): Boolean = hasPermissions


}

internal interface TextComponentDecorator {

    fun getTrailingIcon(text: String): Int?
}

internal class TextComponentDecoratorImpl @Inject constructor(
    private val permissionChecker: PermissionChecker
) : TextComponentDecorator {

    override fun getTrailingIcon(text: String): Int? {
        return when {
            text.isNotEmpty() -> R.drawable.ic_clear
            permissionChecker.hasPermissions() && text.isEmpty() -> R.drawable.ic_location
            else -> null
        }
    }
}