package com.oscarg798.remembrall.auth

import  com.oscarg798.remembrall.auth.ExternalAuthProvider.SignInRequestResult

// Once external login is performed we might need to run some actions
fun interface LoginAction : suspend (SignInRequestResult) -> Unit
