package com.sunasterisk.thooi.ui.category

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.databinding.ItemCategoryJobBinding
import com.sunasterisk.thooi.util.inflater

class UserProfilesAdapter : ListAdapter<Post, PostCategoryVH>(Post.diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCategoryVH =
        PostCategoryVH(parent)

    override fun onBindViewHolder(holder: PostCategoryVH, position: Int) =
        holder.bind(getItem(position))
}

class PostCategoryVH(
    parent: ViewGroup,
) : BaseViewHolder<Post, ItemCategoryJobBinding>(
    ItemCategoryJobBinding.inflate(parent.inflater, parent, false)
) {

    init {
        binding.textPostSummaryPrice.isSelected = true
        binding.textPostSummaryAddress.isSelected = true
    }

    override fun onBind(item: Post, binding: ItemCategoryJobBinding) = with(binding) {
        textTitlePost.text = item.title
        textDescriptionPost.text = item.description
        textPostSummaryAddress.text = item.address
        textPostSummaryPrice.text = item.suggestedPrice.toString()
    }
}

private val postDiffUtil = object : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}

val Post.Companion.diffUtil: DiffUtil.ItemCallback<Post>
    get() = postDiffUtil
