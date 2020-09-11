package com.sunasterisk.thooi.ui.binding

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButton

@BindingAdapter("checkedChanged")
fun MaterialButton.checkedChanged(check: MutableLiveData<Boolean>) =
    addOnCheckedChangeListener { _, isChecked -> check.value = isChecked }
