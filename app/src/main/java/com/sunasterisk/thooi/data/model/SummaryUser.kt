package com.sunasterisk.thooi.data.model

import androidx.recyclerview.widget.DiffUtil
import org.threeten.bp.LocalDateTime

data class SummaryUser(
    val id: String = "",
    val avatarUrl: String = "",
    val fullName: String = "",
    val lastActive: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SummaryUser>() {
            override fun areItemsTheSame(oldItem: SummaryUser, newItem: SummaryUser) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SummaryUser, newItem: SummaryUser) =
                oldItem == newItem
        }
    }
}
