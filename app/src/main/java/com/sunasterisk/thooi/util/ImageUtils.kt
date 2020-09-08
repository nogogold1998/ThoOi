package com.sunasterisk.thooi.util

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

const val DEFAULT_BITMAP_SIZE = 128

fun Context.getBitmapFromRes(@DrawableRes drawableRes: Int, size: Int = DEFAULT_BITMAP_SIZE) =
    ContextCompat.getDrawable(this, drawableRes)?.toBitmap(size, size)
