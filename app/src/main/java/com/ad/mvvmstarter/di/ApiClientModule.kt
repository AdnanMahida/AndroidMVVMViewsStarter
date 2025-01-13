package com.ad.mvvmstarter.di

import android.content.Context
import com.ad.mvvmstarter.BuildConfig
import com.ad.mvvmstarter.preference.AppPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiClientModule {
    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    fun provideAppPreference(
        @ApplicationContext context: Context
    ) = AppPreference(context = context)

    private class AuthorizationInterceptor(val preferences: AppPreference) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val accessToken = preferences.accessToken
            var strToken = ""
            if (accessToken == "") {
                strToken
            } else {
                strToken = "Bearer $accessToken"
            }
            val request = chain.request().newBuilder().addHeader(
                "Authorization", strToken
            ).build()

            Timber.tag("Interceptor").d("intercept: $strToken \naccessToken: $accessToken")
            return chain.proceed(request)
        }
    }


    @Provides
    @Singleton
    fun provideHttpInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .connectTimeout(180, TimeUnit.SECONDS) // 180 seconds
            .writeTimeout(180, TimeUnit.SECONDS) // 180 seconds
            .readTimeout(180, TimeUnit.SECONDS).build() // 180 seconds
    }

}