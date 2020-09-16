package com.sunasterisk.thooi.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.sunasterisk.thooi.R
import jp.wasabeef.glide.transformations.BlurTransformation

fun ImageView.load(
    url: String,
    requestBuilder: (RequestBuilder<Drawable>.() -> Unit) = {
        centerCrop()
    },
) = post {
    if (context == null) return@post
    Glide.with(context)
        .load(Uri.parse(url))
        .apply { requestBuilder.invoke(this) }
        .error(R.drawable.ic_broken_image_24)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun RequestBuilder<Drawable>.blur(radius: Int, sampling: Int) =
    apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
