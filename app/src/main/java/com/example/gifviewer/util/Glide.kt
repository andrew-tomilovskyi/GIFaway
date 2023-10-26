package com.example.gifviewer.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gifviewer.R

fun glideSetGif(context: Context, url: String, view: ImageView) {
    Glide.with(context)
        .asGif()
        .load(url)
        .error(R.drawable.ic_placeholder_error)
        .apply(RequestOptions().centerCrop())
        .into(view)
}

fun glideSetFullScreenGif(context: Context, url: String, view: ImageView) {
    Glide.with(context)
        .asGif()
        .load(url)
        .error(R.drawable.ic_placeholder_error)
        .apply(RequestOptions().fitCenter())
        .into(view)
}