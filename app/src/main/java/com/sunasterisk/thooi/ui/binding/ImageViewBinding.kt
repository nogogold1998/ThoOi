package com.sunasterisk.thooi.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sunasterisk.thooi.util.blur
import com.sunasterisk.thooi.util.load

@BindingAdapter("imageUrl", "blurRadius", "blurSampling")
fun ImageView.loadImage(url: String?, blurRadius: Int?, blurSampling: Int?) {
    if (url != null) {
        this.load(url) {
            if (blurRadius != null && blurSampling != null) {
                blur(blurRadius, blurSampling)
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (url != null) {
        this.load(url)
    }
}
