package com.sunasterisk.thooi.ui.post.newpost

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.databinding.ItemCategoryBinding
import com.sunasterisk.thooi.util.inflater

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.ViewHolder>(Companion) {

    private var onItemClick: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCategoryBinding.inflate(parent.inflater), onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnclickListener(listener: (Category) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding, onItemClick: ((Category) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {
        private var category: Category? = null

        init {
            if (onItemClick != null) itemView.setOnClickListener { category?.let { onItemClick(it) } }
        }

        fun bind(item: Category) {
            category = item
            binding.category = item
        }
    }

    companion object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }
}
