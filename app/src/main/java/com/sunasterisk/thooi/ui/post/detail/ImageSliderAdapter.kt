package com.sunasterisk.thooi.ui.post.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import com.sunasterisk.thooi.databinding.ItemImageSliderBinding
import com.sunasterisk.thooi.util.load

/**
 * Created by Cong Vu Chi on 06/09/20 18:55.
 */
class ImageSliderAdapter : SliderViewAdapter<ImageSliderAdapter.SlideAdapterVH>() {
    private val listItems = mutableListOf<String>()

    override fun getCount() = listItems.size

    override fun onCreateViewHolder(parent: ViewGroup) = SlideAdapterVH(parent)

    override fun onBindViewHolder(viewHolder: SlideAdapterVH, position: Int) {
        viewHolder.bind(listItems[position])
    }

    fun submitList(list: List<String>) {
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    class SlideAdapterVH(
        private val binding: ItemImageSliderBinding,
    ) : SliderViewAdapter.ViewHolder(binding.root) {
        constructor(container: ViewGroup) : this(
            ItemImageSliderBinding.inflate(LayoutInflater.from(container.context), container, false)
        )

        fun bind(url: String) {
            binding.imageSliderBackground.load(url)
        }
    }
}
