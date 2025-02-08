package com.ad.mvvmstarter.utility.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.ad.mvvmstarter.databinding.SnackBarViewBinding
import com.ad.mvvmstarter.utility.AppConstants
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


/**
 * Activity related extensions
 * */
fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(
        window, false
    )
    WindowInsetsControllerCompat(
        window, window.decorView
    ).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(
        window, true
    )
    WindowInsetsControllerCompat(
        window, window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}

fun Activity.changeStatusBarColor(
    color: Int, isLight: Boolean
) {
    val wic = WindowInsetsControllerCompat(
        window, window.decorView
    )
    wic.isAppearanceLightStatusBars = isLight
    window.statusBarColor = color
}

fun Activity.hideKeyboard() {
    val view = findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }
}


fun Activity.showSnackBar(
    message: String?,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_LONG,
    snackBarType: AppConstants.SnackBarType = AppConstants.SnackBarType.ERROR,
    viewGroup: ViewGroup? = null
) {
    var view = this.findViewById<View>(android.R.id.content)

    if (view == null) {
        view = this.window.decorView.findViewById(android.R.id.content)
    }

    if (viewGroup != null) {
        view = viewGroup
    }
    Snackbar.make(
        view, "", duration
    ).apply {
        try {
            val snackBinding = SnackBarViewBinding.inflate(
                layoutInflater, null, false
            )

            this.view.setBackgroundColor(Color.TRANSPARENT)
            this.view.setPadding(
                0, 0, 0, 0
            )

            snackBinding.sbTitleTV.text = message ?: ""

            snackBinding.sbCard.setBgColor(snackBarType.getBackgroundColorRes())
            snackBinding.sbTitleTV.setTextColor(
                ContextCompat.getColor(
                    this.context, snackBarType.getTextColorRes()
                )
            )
            snackBinding.sbIconIV.setColorFilter(
                ContextCompat.getColor(
                    this.context, snackBarType.getTextColorRes()
                )
            )
            val snackBarLayout = this.view as Snackbar.SnackbarLayout
            snackBarLayout.addView(snackBinding.root)
        } catch (e: Exception) {
            Timber.e("SnackBar exception :: ${e.printStackTrace()}")
        }
    }.show()
}

fun Fragment?.showSnackBar(
    message: String?,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT,
    snackBarType: AppConstants.SnackBarType = AppConstants.SnackBarType.ERROR,
    viewGroup: ViewGroup? = null
) {
    val newDuration = if (duration != Snackbar.LENGTH_SHORT && duration != Snackbar.LENGTH_LONG) {
        duration
    } else {
        if ((message?.length ?: 0) > 100) {
            Snackbar.LENGTH_LONG
        } else {
            duration
        }
    }
    (this?.activity as AppCompatActivity?)?.showSnackBar(
        message, newDuration, snackBarType, viewGroup
    )
}

/**
 * Dimensions
 * */
val Number.dp: Float
    get() = toFloat() * Resources.getSystem().displayMetrics.density

val Number.pxOfDP: Float
    get() = toFloat() / Resources.getSystem().displayMetrics.density

val Number.sp: Number
    get() = toFloat() * Resources.getSystem().displayMetrics.scaledDensity

val Number.pxOfSP: Number
    get() = toFloat() / Resources.getSystem().displayMetrics.scaledDensity