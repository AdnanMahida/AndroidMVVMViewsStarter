package com.ad.mvvmstarter.utility.extension

import android.content.Context
import android.widget.Toast
import java.io.File

fun String?.getFileExtension(): String {
    val fileName = File(this ?: "").name
    return if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) fileName.substring(
        fileName.lastIndexOf(".") + 1
    ) else ""
}

fun String.showAsToast(context: Context) {
    showAsToast(
        context,
        Toast.LENGTH_SHORT
    )
}

fun String.showAsToastLong(context: Context) {
    showAsToast(
        context,
        Toast.LENGTH_LONG
    )
}

fun String.showAsToast(
    context: Context,
    duration: Int
) {
    Toast.makeText(
        context,
        this,
        duration
    )
        .show()
}

/**
 * Check this if it's url or not
 * */
fun String?.isUrl(): Boolean {
    return this?.startsWith("http://") == true || this?.startsWith("https://") == true
}
