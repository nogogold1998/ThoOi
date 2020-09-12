package com.sunasterisk.thooi.data.model

import androidx.recyclerview.widget.DiffUtil
import com.sunasterisk.thooi.data.source.entity.Post

/**
 * Created by Cong Vu Chi on 10/09/20 09:54.
 */
data class SummaryPost(
    val id: String,
    val thumbnailUrl: String?,
    val title: String,
    val suggestedPrice: String,
    val address: String,
) {
    constructor(post: Post) : this(post.id,
        post.imagesRefs.randomOrNull(),
        post.title,
        post.suggestedPrice,
        post.address)

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SummaryPost>() {
            override fun areItemsTheSame(oldItem: SummaryPost, newItem: SummaryPost) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SummaryPost, newItem: SummaryPost) =
                oldItem == newItem
        }
    }
}
