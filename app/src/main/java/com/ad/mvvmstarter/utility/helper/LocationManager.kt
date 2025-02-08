package com.ad.mvvmstarter.utility.helper

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

/**
 * Used for getting location
 * */
@SuppressLint("MissingPermission")
class LocationManager(
    private val context: Context,
    private var timeInterval: Long = 10_000, // 10 seconds
    private var minimalDistance: Float = 0F
) : LocationCallback() {
    private var request: LocationRequest
    private var locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var onGetLocation: ((location: Address?) -> Unit)? = null

    init {
        // getting the location client
        request = createRequest()
    }

    private fun createRequest(): LocationRequest =
        // New builder
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            timeInterval
        )
            .apply {
                setMinUpdateDistanceMeters(minimalDistance)
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }
            .build()

    fun changeRequest(
        timeInterval: Long,
        minimalDistance: Float
    ) {
        this.timeInterval = timeInterval
        this.minimalDistance = minimalDistance
        createRequest()
        stopLocationTracking()
        startLocationTracking()
    }

    fun startLocationTracking() =
        locationClient.requestLocationUpdates(
            request,
            this,
            Looper.getMainLooper()
        )

    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    fun setOnGetLocationListener(onGetLocation: ((location: Address?) -> Unit)) {
        this.onGetLocation = onGetLocation
    }

    override fun onLocationResult(location: LocationResult) {
        location.lastLocation?.let {
            setLocation(it)
            stopLocationTracking()
        }
    }

    override fun onLocationAvailability(availability: LocationAvailability) {
        //  react on the availability change
    }

    private fun setLocation(location: Location) {
        Geocoder(
            context,
            Locale.ENGLISH
        )
            .getAddress(
                location.latitude,
                location.longitude
            )
            { address: Address? ->
                onGetLocation?.invoke(address)
            }
    }

    @Suppress("DEPRECATION")
    private fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        address: (Address?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(
                latitude,
                longitude,
                1
            ) { address(it.firstOrNull()) }
            return
        }

        try {
            address(
                getFromLocation(
                    latitude,
                    longitude,
                    1
                )?.firstOrNull()
            )
        } catch (e: Exception) {
            //will catch if there is an internet problem
            address(null)
        }
    }
}