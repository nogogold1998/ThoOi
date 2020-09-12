package com.sunasterisk.thooi.ui.post.newpost

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.thooi.databinding.ItemAddressBinding
import com.sunasterisk.thooi.databinding.ItemNewPostBinding
import com.sunasterisk.thooi.util.inflater
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_new_post.view.*

class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(Companion) {

    private var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemNewPostBinding.inflate(parent.inflater), onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnclickListener(listener: (String) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(val binding: ItemNewPostBinding, onItemClick: ((String) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {
        private var address: String? = null

        init {
            if (onItemClick != null) itemView.fabClose.setOnClickListener { address?.let { onItemClick(it) } }
        }

        fun bind(item: String) {

        }
    }

    companion object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}
