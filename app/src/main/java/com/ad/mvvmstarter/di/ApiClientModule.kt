package com.ad.mvvmstarter.di

import android.content.Context
import com.ad.mvvmstarter.BuildConfig
import com.ad.mvvmstarter.network.DemoService
import com.ad.mvvmstarter.preference.AppPreference
import com.ad.mvvmstarter.utility.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * ApiClientModule used for
 * api related DI
 * */
@Module
@InstallIn(SingletonComponent::class)
class ApiClientModule {
    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    fun provideAppPreference(
        @ApplicationContext context: Context
    ) = AppPreference(context = context)

    class AuthorizationInterceptor(private val preferences: AppPreference) : Interceptor {
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
    fun provideHttpInterceptor(
        preferences: AppPreference
    ) = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(preferences))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(AppConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(AppConstants.READ_TIMEOUT, TimeUnit.SECONDS).build()


    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        httpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.newBuilder().build())
        .build()


    @Provides
    @Singleton
    fun provideDemoService(retrofit: Retrofit): DemoService {
        return retrofit.create(DemoService::class.java)
    }

}