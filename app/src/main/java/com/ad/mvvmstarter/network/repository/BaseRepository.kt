package com.ad.mvvmstarter.network.repository

import okhttp3.OkHttpClient
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    fun getHttpInterceptor() = okHttpClient

    fun callDemoBackgroundSyncApi(id: Int) {

    }
}