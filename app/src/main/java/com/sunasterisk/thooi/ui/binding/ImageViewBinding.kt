package com.sunasterisk.thooi.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.smarteist.autoimageslider.SliderView
import com.sunasterisk.thooi.ui.post.detail.ImageSliderAdapter
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

@BindingAdapter("slideImages")
fun loadImage(sliderView: SliderView, urls: List<String>?) {
    if (urls != null) {
        (sliderView.sliderAdapter as? ImageSliderAdapter)
            ?.submitList(urls)
    }
}
