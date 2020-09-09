package com.sunasterisk.thooi.ui.binding

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun TextInputLayout.bindError(@StringRes resId: Int?) {
    error = resId?.let { context.getString(resId) } ?: ""
}

@BindingAdapter("focusChanged")
fun TextInputEditText.focusChanged(focus: MutableLiveData<Boolean>) =
    setOnFocusChangeListener { _, isFocus -> focus.value = isFocus }
