package com.ad.mvvmstarter.network.repository

import com.ad.mvvmstarter.network.DemoService
import com.ad.mvvmstarter.network.safeApiCall
import okhttp3.OkHttpClient
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val okHttpClient: OkHttpClient, private val demoService: DemoService
) {
    fun getHttpInterceptor() = okHttpClient

    fun callDemoBackgroundSyncApi(id: Int) {

    }

    suspend fun callGetListOfDevices(onError: (statusCode: Int, message: String) -> Unit) =
        safeApiCall(onError = onError, callFunction = { demoService.getListOfDevices() })

}