package com.ad.mvvmstarter.utility.extension

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.WindowManager
import androidx.core.content.ContextCompat


fun Context.getWindowDimension(): Pair<Int, Int> {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return Pair(size.x, size.y)
}


fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    cm?.run {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }
    return result
}

fun Context.getDrawableCompat(resourceId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resourceId)
}

fun Context.getAppVersionName(): String {
    return try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        pInfo.versionName.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        "N/A"
    }
}

fun Context.getAppVersion(): String {
    try {
        val pInfo = packageManager.getPackageInfo(packageName, 0);
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode.toString()
        } else {
            pInfo.versionCode.toString()
        };
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace();
    }
    return "0"
}


fun Context.openGoogleMap(address: String, onAppNotAvailable: (() -> Unit?)? = null) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?q=$address")
        )
        startActivity(intent)
    } catch (e: Exception) {
        onAppNotAvailable?.invoke()
    }
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}
