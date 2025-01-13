package com.ad.mvvmstarter.utility.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
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
import java.io.File


private fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(
        window,
        false
    )
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

private fun Activity.showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(
        window,
        true
    )
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}

fun Activity.changeStatusBarColor(
    color: Int,
    isLight: Boolean
) {
    val wic = WindowInsetsControllerCompat(
        window,
        window.decorView
    )
    wic.isAppearanceLightStatusBars = isLight
    window.statusBarColor = color
}

/**
Extension functions for showing snack bar from Activity and Fragment
 */
fun AppCompatActivity.showSnackBar(
    message: String?,
    @BaseTransientBottomBar.Duration
    duration: Int = Snackbar.LENGTH_LONG,
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
        view,
        "",
        duration
    )
        .apply {
            try {
                val snackBinding = SnackBarViewBinding.inflate(
                    layoutInflater,
                    null,
                    false
                )

                this.view.setBackgroundColor(Color.TRANSPARENT)
                this.view.setPadding(
                    0,
                    0,
                    0,
                    0
                )

                snackBinding.sbTitleTV.text = message ?: ""

                snackBinding.sbCard.setBgColor(snackBarType.getBackgroundColorRes())
                snackBinding.sbTitleTV.setTextColor(
                    ContextCompat.getColor(
                        this.context,
                        snackBarType.getTextColorRes()
                    )
                )
                snackBinding.sbIconIV.setColorFilter(
                    ContextCompat.getColor(
                        this.context,
                        snackBarType.getTextColorRes()
                    )
                )
                val snackBarLayout = this.view as Snackbar.SnackbarLayout
                snackBarLayout.addView(snackBinding.root)
            } catch (e: Exception) {
                Timber.e("SnackBar exception :: ${e.printStackTrace()}")
            }
        }
        .show()
}

fun Fragment?.showSnackBar(
    message: String?,
    @BaseTransientBottomBar.Duration
    duration: Int = Snackbar.LENGTH_SHORT,
    snackBarType: AppConstants.SnackBarType = AppConstants.SnackBarType.ERROR,
    viewGroup: ViewGroup? = null
) {
    val newDuration =
        if (duration != Snackbar.LENGTH_SHORT && duration != Snackbar.LENGTH_LONG) {
            duration
        } else {
            if ((message?.length ?: 0) > 100) {
                Snackbar.LENGTH_LONG
            } else {
                duration
            }
        }
    (this?.activity as AppCompatActivity?)?.showSnackBar(
        message,
        newDuration,
        snackBarType,
        viewGroup
    )
}

/**
 * Checking file size
 */
fun File.isFileLessThanMiBs(
    mb: Int
): Boolean {
    val maxFileSize = mb * 1024 * 1024
    val l = length()
    val fileSize = l.toString()
    val finalFileSize = fileSize.toInt()
    return finalFileSize >= maxFileSize
}


fun Spinner.selected(action: (position: Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            action(position)
        }
    }
}

fun AppCompatTextView.setBGDrawable(@DrawableRes drawableInt: Int) {
    setBackgroundDrawable(ContextCompat.getDrawable(this.context, drawableInt))
}

fun AppCompatTextView.setBGColorTint(@ColorRes color: Int) {
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}

fun AppCompatTextView.setLabelColor(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(this.context, color))
}

fun AppCompatTextView.setDrawableColorTint(@ColorRes color: Int) {
    val colorFilter =
        PorterDuffColorFilter(ContextCompat.getColor(this.context, color), PorterDuff.Mode.SRC_IN)
    this.compoundDrawablesRelative.forEach { drawable ->
        if (drawable != null) {
            drawable.colorFilter = colorFilter
        }
    }
}

fun AppCompatTextView.applyColors(isSelected: Boolean, selectedColor: Int, unselectedColor: Int) {
    val color = if (isSelected) selectedColor else unselectedColor
    setBGColorTint(color)
    setDrawableColorTint(color)
    setLabelColor(color)
}


fun Context.openAppSystemSettings() {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    })
}

fun Double.clean(): String = if (this % 1 == 0.0) String.format("%.0f", this) else this.toString()

fun Double.roundToTwoDecimalPlaces(): Double {
    return String.format("%.2f", this).toDouble()
}
