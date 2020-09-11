package com.sunasterisk.thooi.util

import android.content.Context
import android.widget.Toast

/**
 * Created by Cong Vu Chi on 08/09/20 08:41.
 */
fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}
