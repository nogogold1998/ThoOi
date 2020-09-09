package com.sunasterisk.thooi.ui.post.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.databinding.ItemDetailsPostActionsCustomerBinding
import com.sunasterisk.thooi.databinding.ItemDetailsPostBinding
import com.sunasterisk.thooi.databinding.ItemSummaryUserBinding
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterItem
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterViewType
import com.sunasterisk.thooi.util.inflater

/**
 * Created by Cong Vu Chi on 06/09/20 23:08.
 */
class PostDetailsAdapter : ListAdapter<PostDetailsAdapterItem<*>, BaseViewHolder<*, *>>(
    PostDetailsAdapterItem.diffUtil
) {

    override fun getItemViewType(position: Int) = getItem(position).viewType.layoutRes

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<*, *> {
        val type =
            PostDetailsAdapterViewType.values().firstOrNull { it.layoutRes == viewType }
                ?: throw "Cannot not find a match for PostDetailsAdapterViewType, viewType was: $viewType"
                    .let(::IllegalArgumentException)

        return when (type) {
            PostDetailsAdapterViewType.POST_DETAILS -> PostDetailsVH(parent)
            PostDetailsAdapterViewType.SUMMARY_USER -> SummaryUserVH(parent)
            PostDetailsAdapterViewType.ACTION_BOTTOM -> ActionBottomVH(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as? BaseViewHolder<PostDetailsAdapterItem<*>, *>)?.bind(getItem(position))
    }

    class PostDetailsVH(container: ViewGroup) :
        BaseViewHolder<PostDetailsAdapterItem.PostDetailsItem, ItemDetailsPostBinding>(
            ItemDetailsPostBinding.inflate(container.inflater, container, false)
        ) {
        override fun onBind(
            item: PostDetailsAdapterItem.PostDetailsItem,
            binding: ItemDetailsPostBinding,
        ) = with(binding) {
            postDetail = item.data
            executePendingBindings()
        }
    }

    class SummaryUserVH(container: ViewGroup) :
        BaseViewHolder<PostDetailsAdapterItem.SummaryUserItem, ItemSummaryUserBinding>(
            ItemSummaryUserBinding.inflate(container.inflater, container, false)
        ) {

        override fun onBind(
            item: PostDetailsAdapterItem.SummaryUserItem,
            binding: ItemSummaryUserBinding,
        ) = with(binding.layoutSummaryUser) {
            user = item.data
            executePendingBindings()
        }
    }

    class ActionBottomVH(container: ViewGroup) :
        BaseViewHolder<PostDetailsAdapterItem.ActionBottomItem, ItemDetailsPostActionsCustomerBinding>(
            ItemDetailsPostActionsCustomerBinding.inflate(container.inflater, container, false)
        ) {

        override fun onBind(
            item: PostDetailsAdapterItem.ActionBottomItem,
            binding: ItemDetailsPostActionsCustomerBinding,
        ) = Unit
    }
}
