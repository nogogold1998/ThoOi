package com.sunasterisk.thooi.ui.post.newpost

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunasterisk.thooi.databinding.ItemNewPostBinding
import com.sunasterisk.thooi.util.inflater
import kotlinx.android.synthetic.main.item_new_post.view.*
import java.io.File

class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(Companion) {

    private var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemNewPostBinding.inflate(parent.inflater), onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnclickListener(listener: (Int) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(val binding: ItemNewPostBinding, onItemClick: ((Int) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {
        private var path: String? = null

        init {
            if (onItemClick != null) itemView.fabClose.setOnClickListener {
                onItemClick(layoutPosition)
            }
        }

        fun bind(item: String) {
            path = item
            binding.path = item
            Glide.with(itemView.context)
                .load(File(item))
                .fitCenter()
                .transition(DrawableTransitionOptions().crossFade())
                .into(binding.imagePost)
        }
    }

    companion object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}
