package com.oscarg798.remembrall.common.network.di

import com.google.gson.Gson
import com.oscarg798.remembrall.BuildConfig
import com.oscarg798.remembrall.common.network.interceptor.GoogleAuthInterceptor
import com.oscarg798.remembrall.common_calendar.data.restclient.CalendarRestClient
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideLoggingInterceptor(): Interceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    } else {
        Interceptor { chain -> chain.proceed(chain.request()) }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: Interceptor,
        googleAuthInterceptor: GoogleAuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor = loggingInterceptor)
        .addInterceptor(googleAuthInterceptor)
        .connectTimeout(ConnectionTimeOut, TimeUnit.SECONDS)
        .readTimeout(ReadTimeOut, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Reusable
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(OAuthUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    @Reusable
    fun provideCalendarService(retrofit: Retrofit): CalendarRestClient =
        retrofit.create(CalendarRestClient::class.java)
}

private const val ReadTimeOut = 30L
private const val ConnectionTimeOut = 30L
private const val OAuthUrl = "https://www.googleapis.com/"
