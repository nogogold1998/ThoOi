package com.sunasterisk.thooi.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T, B : ViewBinding>(
    protected val binding: B,
) : RecyclerView.ViewHolder(binding.root) {
    protected var cachedValue: T? = null

    fun bind(item: T) {
        cachedValue = item
        onBind(item, binding)
    }

    open fun onBind(item: T, binding: B) = Unit
}
