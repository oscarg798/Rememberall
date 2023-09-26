package com.oscarg798.remembrall.oauthimpl.di

import com.google.gson.Gson
import com.oscarg798.remembrall.BuildConfig
import com.oscarg798.remembrall.OAuthClient
import com.oscarg798.remembrall.oauthimpl.DateProvider
import com.oscarg798.remembrall.oauthimpl.DateProviderImpl
import com.oscarg798.remembrall.oauthimpl.OAuthClientImpl
import com.oscarg798.remembrall.oauthimpl.network.OAuthEndPoint
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal interface OAuthClientModule {

    @Binds
    fun bindOAuthClient(impl: OAuthClientImpl): OAuthClient

    @Binds
    @Reusable
    fun bindDataProvider(impl: DateProviderImpl): DateProvider

    companion object {

        @IntoSet
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
        @OAuthHttpClient
        fun provideHttpClient(
            interceptors: Set<@JvmSuppressWildcards Interceptor>,
        ): OkHttpClient {
            val builder = OkHttpClient.Builder()
                .connectTimeout(TimeOut, TimeUnit.SECONDS)
                .readTimeout(TimeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)

            interceptors.forEach {
                builder.addInterceptor(it)
            }

            return builder.build()
        }

        @Provides
        @Reusable
        @OAuthRetrofit
        fun provideRetrofit(
            @OAuthHttpClient okHttpClient: OkHttpClient,
        ): Retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .build()

        @Provides
        fun provideOAuthEndpoint(@OAuthRetrofit retrofit: Retrofit): OAuthEndPoint {
            return retrofit.create(OAuthEndPoint::class.java)
        }
    }
}

@Qualifier
annotation class OAuthRetrofit

@Qualifier
annotation class OAuthHttpClient

private const val TimeOut = 30L
private const val BaseUrl = "https://www.googleapis.com/"
