package com.oscarg798.remembrall.common_auth.extensions

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

fun GoogleSignInAccount.getUserName() = when {
    this.displayName != null -> this.displayName!!
    this.givenName != null && this.familyName != null -> "${this.givenName} ${this.familyName}"
    this.givenName != null -> this.givenName!!
    else -> "Unknown"
}
