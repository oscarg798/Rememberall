package com.recipegenius.authimpl.di

import com.google.firebase.auth.FirebaseAuth
import com.oscarg798.remembrall.android.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.auth.AuthOptions
import com.oscarg798.remembrall.auth.ExternalAuthProvider
import com.oscarg798.remembrall.auth.LoginAction
import com.oscarg798.remembrall.auth.LogoutAction
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.config.Config
import com.recipegenius.authimpl.GoogleAuthOptionsBuilderImpl
import com.recipegenius.authimpl.OneTapAuthProvider
import com.recipegenius.authimpl.SessionImpl
import com.recipegenius.authimpl.login.FinishLoginAction
import com.recipegenius.authimpl.login.RequestScopesLoginAction
import com.recipegenius.authimpl.logout.FirebaseLogoutAction
import com.recipegenius.authimpl.logout.GoogleSignInLogoutAction
import com.recipegenius.authimpl.logout.IdentityLogoutAction
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthBindings {

    @Binds
    fun bindGoogleAuthOptionsBuilder(impl: GoogleAuthOptionsBuilderImpl): GoogleAuthOptionsBuilder

    @Binds
    @IntoSet
    fun bindGoogleSignInLogoutAction(impl: GoogleSignInLogoutAction): LogoutAction

    @Binds
    @IntoSet
    fun bindFirebaseLogoutAction(impl: FirebaseLogoutAction): LogoutAction

    @Binds
    @IntoSet
    fun bindIdentityLogoutAction(impl: IdentityLogoutAction): LogoutAction
}

@Module
@InstallIn(ViewModelComponent::class)
internal interface AuthProviderModule {

    @Binds
    @ViewModelScoped
    fun bindExternalAuthProvider(impl: OneTapAuthProvider): ExternalAuthProvider
}


@Module
@InstallIn(SingletonComponent::class)
internal interface LoginActionBindings {

    @Binds
    @IntoSet
    fun bindRequestScopesLoginAction(impl: RequestScopesLoginAction): LoginAction

    @Binds
    @IntoSet
    fun bindLoginAction(impl: FinishLoginAction): LoginAction
}

@Module
@InstallIn(SingletonComponent::class)
internal interface SessionBindings {

    @Binds
    @Singleton
    fun bindSession(impl: SessionImpl): Session
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Reusable
    fun provideAuthOptions(config: Config): AuthOptions = AuthOptions(
        requestServerAuth = true,
        requestProfileInfo = true,
        requestIdToken = true,
        scopes = setOf(CalendarScope),
        clientId = config.clientId
    )

}

private const val CalendarScope = "https://www.googleapis.com/auth/calendar"