package com.sunasterisk.thooi.util

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ScrollingView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Cong Vu Chi on 06/09/20 23:18.
 */
val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View.snackbar(msg: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, duration).show()
}

val ScrollingView.verticalScrollProgress: Float
    get() {
        val offset = computeVerticalScrollOffset()
        val extent = computeVerticalScrollExtent()
        val range = computeVerticalScrollRange()

        return offset / (range - extent).toFloat()
    }

fun RecyclerView.scrollToPositionScrollChangeListenerAware(pos: Int) {
    scrollBy(0, 1)
    scrollToPosition(pos)
}
