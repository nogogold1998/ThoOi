package com.sunasterisk.thooi.util

import android.view.LayoutInflater
import android.view.View

/**
 * Created by Cong Vu Chi on 06/09/20 23:18.
 */
val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)
