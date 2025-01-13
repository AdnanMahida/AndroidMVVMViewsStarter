package com.ad.mvvmstarter.utility.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true) // Network is available
        }

        override fun onLost(network: Network) {
            postValue(false) // Network is lost
        }

        override fun onUnavailable() {
            postValue(false) // Network is unavailable
        }
    }

    override fun onActive() {
        super.onActive()
        val activeNetwork = connectivityManager.activeNetwork
        val isConnected = activeNetwork?.let {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(it)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } ?: false

        postValue(isConnected)

        // Register callback for monitoring
        val networkRequest =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        // Unregister callback to avoid memory leaks
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
