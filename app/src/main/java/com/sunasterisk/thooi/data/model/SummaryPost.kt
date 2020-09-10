package com.sunasterisk.thooi.data.model

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Cong Vu Chi on 10/09/20 09:54.
 */
data class SummaryPost(
    val id: String,
    val thumbnailUrl: String,
    val title: String,
    val suggestedPrice: String,
    val address: String,
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SummaryPost>() {
            override fun areItemsTheSame(oldItem: SummaryPost, newItem: SummaryPost) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SummaryPost, newItem: SummaryPost) =
                oldItem == newItem
        }
    }
}
