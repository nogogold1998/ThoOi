package com.sunasterisk.thooi.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface BindingComponent<out B : ViewBinding> {
    fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean = false,
    ): B
}
