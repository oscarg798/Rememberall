package com.oscarg798.remembrall.auth

interface LogoutAction : suspend (Session.State.LoggedIn) -> Unit