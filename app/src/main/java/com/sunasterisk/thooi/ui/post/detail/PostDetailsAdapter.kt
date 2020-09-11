package com.sunasterisk.thooi.ui.post.detail

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.databinding.ItemDetailsPostActionsCustomerBinding
import com.sunasterisk.thooi.databinding.ItemDetailsPostBinding
import com.sunasterisk.thooi.databinding.ItemSummaryUserBinding
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAction
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterItem
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterViewType
import com.sunasterisk.thooi.util.inflater

/**
 * Created by Cong Vu Chi on 06/09/20 23:08.
 */
class PostDetailsAdapter(
    private val clickListener: ClickListener,
) : ListAdapter<PostDetailsAdapterItem<*>, BaseViewHolder<*, *>>(
    PostDetailsAdapterItem.diffUtil
) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

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
            PostDetailsAdapterViewType.SUMMARY_USER -> SummaryUserVH(parent, clickListener)
            PostDetailsAdapterViewType.ACTION_BOTTOM -> {
                CustomerActionBottomVH(parent, clickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as? BaseViewHolder<PostDetailsAdapterItem<*>, *>)?.bind(getItem(position))
    }

    fun interface ClickListener {
        fun onClick(action: PostDetailsAction)
    }

    class PostDetailsVH(container: ViewGroup) :
        BaseViewHolder<PostDetailsAdapterItem.PostDetailsItem, ItemDetailsPostBinding>(
            ItemDetailsPostBinding.inflate(container.inflater, container, false)
        ) {

        init {
            binding.layoutSummaryCustomer.textUserFullName.isSelected = true
        }

        override fun onBind(
            item: PostDetailsAdapterItem.PostDetailsItem,
            binding: ItemDetailsPostBinding,
        ) = with(binding) {
            postDetail = item.data
            executePendingBindings()
        }
    }

    class SummaryUserVH(
        container: ViewGroup,
        clickListener: ClickListener,
    ) : BaseViewHolder<PostDetailsAdapterItem.SummaryUserItem, ItemSummaryUserBinding>(
        ItemSummaryUserBinding.inflate(container.inflater, container, false)
    ) {

        init {
            binding.root.setOnClickListener {
                cachedValue?.data
                    ?.let(PostDetailsAction::SelectFixer)
                    ?.let(clickListener::onClick)
            }
            binding.layoutSummaryUser.textUserFullName.isSelected = true
        }

        override fun onBind(
            item: PostDetailsAdapterItem.SummaryUserItem,
            binding: ItemSummaryUserBinding,
        ) = with(binding.layoutSummaryUser) {
            imageSelectedTick.isVisible = item.isSelected
            user = item.data
            executePendingBindings()
        }
    }

    class CustomerActionBottomVH(
        container: ViewGroup,
        clickListener: ClickListener,
    ) : BaseViewHolder<PostDetailsAdapterItem.ActionBottomItem, ItemDetailsPostActionsCustomerBinding>(
        ItemDetailsPostActionsCustomerBinding.inflate(container.inflater, container, false)
    ) {

        init {
            binding.actionListener = clickListener
        }

        override fun onBind(
            item: PostDetailsAdapterItem.ActionBottomItem,
            binding: ItemDetailsPostActionsCustomerBinding,
        ) = with(binding) {
            val status = item.data
            buttonAssignFixer.isGone = status != PostStatus.OPEN
            buttonReassignFixer.isGone = status != PostStatus.PENDING
            buttonClosePost.isGone = status in arrayOf(
                PostStatus.ON_PROGRESS,
                PostStatus.PENDING,
                PostStatus.FINISHED
            )
            buttonCancelFixing.isGone = status != PostStatus.ON_PROGRESS
            buttonFinishJob.isGone = status !in arrayOf(
                PostStatus.ON_PROGRESS,
                PostStatus.FINISHED,
            )
            postStatus = status
            executePendingBindings()
        }
    }
}
