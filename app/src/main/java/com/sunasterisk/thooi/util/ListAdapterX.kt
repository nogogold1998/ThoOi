package com.sunasterisk.thooi.util

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

fun <T : Any, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.submitListWithMotionLayoutAware(
    recyclerView: RecyclerView,
    list: List<T>?,
    commitCallback: Runnable? = null,
) {
    submitList(list, commitCallback)
    recyclerView.scrollBy(0, 0)
}
