package com.ad.mvvmstarter.services


import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ad.mvvmstarter.R
import com.ad.mvvmstarter.di.LocationServiceEntryPoint
import com.ad.mvvmstarter.network.repository.BaseRepository
import com.ad.mvvmstarter.ui.MainActivity
import com.ad.mvvmstarter.utility.helper.DefaultLocationClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var locationClient: LocationClient
    private lateinit var locationRepository: BaseRepository

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext, LocationServiceEntryPoint::class.java
        )

        locationRepository = entryPoint.provideBaseRepository()

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start(this)
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("NotificationPermission")
    private fun start(context: Context) {

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0, // Unique request code (can be any integer)
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT // Flag to update if already exists
        )

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Location Tracking...").setContentText("Location: ")
            .setSmallIcon(R.drawable.ic_launcher_background).setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdates(interval = 10_000L)   //10 seconds
            .catch { e ->
                e.printStackTrace()
                Timber.e("LOCATION ERROR ${e.printStackTrace()}")
            }.onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)",
                )
                notificationManager.notify(1, updatedNotification.build())

            }.launchIn(serviceScope)
        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}