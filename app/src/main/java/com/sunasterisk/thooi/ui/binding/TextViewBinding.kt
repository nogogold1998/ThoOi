package com.sunasterisk.thooi.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Cong Vu Chi on 03/09/20 16:04.
 */
@BindingAdapter("localDateTime")
fun TextView.bindLocalDateTime(localDateTime: LocalDateTime?) {
    text = localDateTime?.format(DateTimeFormatter.ISO_DATE_TIME)
}
