package com.sunasterisk.thooi.ui.post.detail.model

import androidx.recyclerview.widget.DiffUtil
import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterViewType.*

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

    class ActionBottomItem(
        override val data: UserType,
    ) : PostDetailsAdapterItem<UserType>(
        when (data) {
            UserType.CUSTOMER -> CUSTOMER_ACTION_BOTTOM
            UserType.FIXER -> FIXER_ACTION_BOTTOM
        }
    ) {
        override fun isTheSame(other: PostDetailsAdapterItem<*>) = other is ActionBottomItem

        override fun isContentTheSame(other: PostDetailsAdapterItem<*>) =
            other is ActionBottomItem && data == other.data
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
