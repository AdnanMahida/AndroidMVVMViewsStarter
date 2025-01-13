package com.ad.mvvmstarter.utility.extension

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.webkit.WebView

fun WebView.loadHtmlText(text: String) {
    loadData(text, "text/html", "UTF-8")
}

/**
 * to handle click event for webview
 * because webview click event is not supported natively
 */
@SuppressLint("ClickableViewAccessibility")
fun WebView.onTouchListener(callback: () -> Unit) {
    var clickedTime: Long = 0
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                clickedTime = event.eventTime
            }

            MotionEvent.ACTION_UP -> {
                if (event.eventTime - clickedTime <= 100) {
                    callback()
                }
            }
        }
        return@setOnTouchListener true
    }
}