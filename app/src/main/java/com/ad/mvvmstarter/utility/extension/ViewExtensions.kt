package com.ad.mvvmstarter.utility.extension

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun hideKeyboard(activity: Activity?) {
    val view = activity?.findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }
}

fun View.clickWithDebounce(
    debounceTime: Long = 700L,
    action: () -> Unit
) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setMargin(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val params = (layoutParams as? ViewGroup.MarginLayoutParams)
    params?.setMargins(
        left ?: params.leftMargin,
        top ?: params.topMargin,
        right ?: params.rightMargin,
        bottom ?: params.bottomMargin
    )
    layoutParams = params
}

fun View.setMarginRes(
    @DimenRes
    left: Int? = null,
    @DimenRes
    top: Int? = null,
    @DimenRes
    right: Int? = null,
    @DimenRes
    bottom: Int? = null
) {
    setMargin(
        if (left == null) null else resources.getDimensionPixelSize(left),
        if (top == null) null else resources.getDimensionPixelSize(top),
        if (right == null) null else resources.getDimensionPixelSize(right),
        if (bottom == null) null else resources.getDimensionPixelSize(bottom),
    )
}

fun MaterialButton.setBgColor(
    @ColorRes
    color: Int
) {
    backgroundTintList = ColorStateList.valueOf(
        ContextCompat.getColor(
            this.context,
            color
        )
    )
}

fun CardView.setBgColor(
    @ColorRes
    color: Int
) {
    backgroundTintList = ColorStateList.valueOf(
        ContextCompat.getColor(
            this.context,
            color
        )
    )
}