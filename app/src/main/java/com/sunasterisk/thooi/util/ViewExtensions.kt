package com.sunasterisk.thooi.util

import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Cong Vu Chi on 06/09/20 23:18.
 */
val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View.snackbar(msg: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, duration).show()
}
