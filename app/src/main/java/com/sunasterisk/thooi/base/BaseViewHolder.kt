package com.sunasterisk.thooi.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<in T, B : ViewBinding>(
    private val binding: B,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) = onBind(item, binding)

    abstract fun onBind(item: T, binding: B)
}
