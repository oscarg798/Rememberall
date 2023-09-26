package com.oscarg798.remembrall.android

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oscarg798.remembrall.auth.AuthOptions

interface GoogleAuthOptionsBuilder : (AuthOptions) -> GoogleSignInOptions
