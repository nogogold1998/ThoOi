package com.sunasterisk.thooi.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sunasterisk.thooi.util.format
import org.threeten.bp.LocalDateTime

/**
 * Created by Cong Vu Chi on 03/09/20 16:04.
 */
@BindingAdapter("localDateTime")
fun TextView.bindLocalDateTime(localDateTime: LocalDateTime?) {
    text = localDateTime?.format()
}
