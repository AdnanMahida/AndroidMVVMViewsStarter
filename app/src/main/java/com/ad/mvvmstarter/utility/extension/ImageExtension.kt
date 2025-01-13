package com.ad.mvvmstarter.utility.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File


fun ImageView.loadImage(
    url: String? = null,
    file: File? = null,
    placeHolder: Int = com.ad.mvvmstarter.R.drawable.ic_launcher_background
) {
    if (url?.trim()?.isNotEmpty() == true && url.trim().lowercase() != "null") {
        val requestManager = Glide.with(this.context.applicationContext)
            .setDefaultRequestOptions(RequestOptions().timeout(60_000)).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        requestManager.apply(RequestOptions().placeholder(placeHolder).error(placeHolder))
            .into(this)
    } else if (file != null) {
        val requestManager = Glide.with(this.context.applicationContext).load(file)
        requestManager.apply(RequestOptions().placeholder(placeHolder).error(placeHolder))
            .into(this)
    } else {
        setImageResource(placeHolder)
    }
}
