package com.sunasterisk.thooi.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<in T, out B : ViewBinding> (
    private val binding: B
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) = onBind(item, binding)

    abstract fun <B> onBind(item: T, binding: B)
}
