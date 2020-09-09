package com.sunasterisk.thooi.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <X, Y> LiveData<X>.transform(transform: (X) -> Y) = MediatorLiveData<Y>().also {
    it.addSource(this) { value -> it.value = transform(value) }
}
