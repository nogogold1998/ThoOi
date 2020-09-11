package com.sunasterisk.thooi.ui.post.detail.model

import androidx.recyclerview.widget.DiffUtil
import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterItem.*
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterViewType.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

sealed class PostDetailsAdapterItem<T>(val viewType: PostDetailsAdapterViewType) {
    abstract val data: T
    abstract fun isTheSame(other: PostDetailsAdapterItem<*>): Boolean
    abstract fun isContentTheSame(other: PostDetailsAdapterItem<*>): Boolean

    data class PostDetailsItem(
        override val data: PostDetail,
    ) : PostDetailsAdapterItem<PostDetail>(POST_DETAILS) {
        override fun isTheSame(other: PostDetailsAdapterItem<*>) =
            other is PostDetailsItem && data.id == other.data.id

        override fun isContentTheSame(other: PostDetailsAdapterItem<*>) =
            other is PostDetailsItem && this.data == other.data
    }

    data class SummaryUserItem(
        override val data: SummaryUser,
        val isSelected: Boolean = false,
    ) : PostDetailsAdapterItem<SummaryUser>(SUMMARY_USER) {
        override fun isTheSame(other: PostDetailsAdapterItem<*>) =
            other is SummaryUserItem && data.id == other.data.id

        override fun isContentTheSame(other: PostDetailsAdapterItem<*>) =
            other is SummaryUserItem && this.data == other.data && isSelected == other.isSelected
    }

    data class ActionBottomItem(
        override val data: PostStatus,
    ) : PostDetailsAdapterItem<PostStatus>(ACTION_BOTTOM) {
        override fun isTheSame(other: PostDetailsAdapterItem<*>) = other is ActionBottomItem

        override fun isContentTheSame(other: PostDetailsAdapterItem<*>) = this == other
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostDetailsAdapterItem<*>>() {
            override fun areItemsTheSame(
                oldItem: PostDetailsAdapterItem<*>,
                newItem: PostDetailsAdapterItem<*>,
            ) = oldItem.isTheSame(newItem)

            override fun areContentsTheSame(
                oldItem: PostDetailsAdapterItem<*>,
                newItem: PostDetailsAdapterItem<*>,
            ) = oldItem.isContentTheSame(newItem)
        }
    }
}

suspend fun PostDetail.toPostDetailsAdapterItem(selectedFixerId: String) =
    coroutineScope {
        val middle = async {
            appliedFixers.map { summaryUser ->
                val isSelected =
                    summaryUser.id == selectedFixerId || summaryUser.id == assignedFixerId
                SummaryUserItem(summaryUser, isSelected)
            }.toTypedArray()
        }
        val top = PostDetailsItem(this@toPostDetailsAdapterItem)
        val bottom = ActionBottomItem(status)
        listOf(top, *middle.await(), bottom)
    }
