package com.ad.mvvmstarter.services

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import com.ad.mvvmstarter.utility.dialog.DialogUtils
import timber.log.Timber

object LocationHelper {
    /**
     * Check if location enable or not if not then redirect to setting screen
     * */
    fun ifLocationEnable(context: Context, onLocationEnable: (Boolean) -> Unit) {
        try {
            val locationManager =

                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                onLocationEnable.invoke(true)
            } else {
                DialogUtils.showConfirmationDialog(context = context,
                    title = "Enable Location",
                    onPositiveCallback = {
                        val intent =
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                        onLocationEnable.invoke(false)
                    },
                    onNegativeCallback = {
                        onLocationEnable.invoke(false)
                    })
            }
        } catch (ex: java.lang.Exception) {
            Timber.e("exception $ex")
        }
    }

    fun isLocationAvailable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}