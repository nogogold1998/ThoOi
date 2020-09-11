package com.sunasterisk.thooi.util

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.transition.Transition
import androidx.transition.TransitionManager.beginDelayedTransition

const val DEFAULT_DURATION = 250L

fun ViewGroup.beginTransition(
    transition: Transition,
    @IdRes vararg viewIds: Int
) {
    beginDelayedTransition(this, transition.apply {
        duration = DEFAULT_DURATION
        viewIds.forEach { addTarget(it) }
    })
}

