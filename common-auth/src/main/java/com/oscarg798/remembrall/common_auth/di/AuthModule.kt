package com.oscarg798.remembrall.common_auth.di

import com.google.firebase.auth.FirebaseAuth
import com.oscarg798.remembrall.common.auth.AuthRepository
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import com.oscarg798.remembrall.common_auth.network.restclient.ExternalSignInClient
import com.oscarg798.remembrall.common_auth.network.restclient.GoogleSignInClient
import com.oscarg798.remembrall.common_auth.repository.data.GoogleAuthRepository
import com.oscarg798.remembrall.config.Config
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
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

    @Provides
    @Reusable
    fun provideGoogleAuthRepository(
        googleSignInRepositoryImpl:
        GoogleAuthRepository
    ): AuthRepository = googleSignInRepositoryImpl


    @Provides
    @Singleton
    fun provideExternalSignInClient(
        googleSignInClient:
        GoogleSignInClient
    ): ExternalSignInClient = googleSignInClient

}

private const val CalendarScope = "https://www.googleapis.com/auth/calendar"
