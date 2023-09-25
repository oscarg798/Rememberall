package com.oscarg798.remembrall.login

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.oscarg798.remembrall.common.auth.AuthRepository
import com.oscarg798.remembrall.common.model.Config
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.provider.StringProviderImpl
import com.oscarg798.remembrall.auth.AuthOptions
import com.oscarg798.remembrall.common_calendar.data.restclient.CalendarRestClient
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers


@InstallIn(SingletonComponent::class)
@Module
object LoginModule {

    @Provides
    fun provideConfig(): Config = Config("123")

    @Provides
    fun provideStringProvider(@ApplicationContext context: Context) : StringProvider = StringProviderImpl(context)

    @Provides
    fun provideSharedPreferences() : SharedPreferences = mockk()

    @Provides
    fun provideCalendarRestClient(): CalendarRestClient = mockk()

    @Provides
    fun provideGson(): Gson = mockk()

    @Provides
    fun provideCoroutineContextProvider() : com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider = object:
        com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider {
        override val io: CoroutineContext
            get() = Dispatchers.IO
        override val computation: CoroutineContext
            get() = Dispatchers.Default
        override val main: CoroutineContext
            get() = Dispatchers.Main

    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = mockk()

    @Provides
    @Reusable
    fun provideAuthOptions(config: Config): AuthOptions = AuthOptions(
        requestServerAuth = true,
        requestProfileInfo = true,
        requestIdToken = true,
        scopes = setOf("CalendarScope"),
        clientId = config.clientId
    )

    @Provides
    @Reusable
    fun provideGoogleAuthRepository(): AuthRepository = mockk()


    @Provides
    @Singleton
    fun provideExternalSignInClient(
    ): ExternalSignInClient = mockk(relaxed = true)
}