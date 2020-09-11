package com.sunasterisk.thooi.ui.post.detail

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.source.entity.PostStatus.*
import com.sunasterisk.thooi.databinding.ItemDetailsPostActionsCustomerBinding
import com.sunasterisk.thooi.databinding.ItemDetailsPostActionsFixerBinding
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
    private val postDetailLive: LiveData<PostDetail>,
    private val lifecycleOwner: LifecycleOwner,
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
            PostDetailsAdapterViewType.CUSTOMER_ACTION_BOTTOM -> {
                CustomerActionBottomVH(parent, postDetailLive, lifecycleOwner, clickListener)
            }
            PostDetailsAdapterViewType.FIXER_ACTION_BOTTOM -> {
                FixerActionBottomVH(parent, postDetailLive, lifecycleOwner, clickListener)
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
                    ?.let(PostDetailsAction.CustomerAction::SelectFixer)
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
        postDetailLive: LiveData<PostDetail>,
        lifecycleOwner: LifecycleOwner,
        clickListener: ClickListener,
    ) : BaseViewHolder<PostDetailsAdapterItem.ActionBottomItem, ItemDetailsPostActionsCustomerBinding>(
        ItemDetailsPostActionsCustomerBinding.inflate(container.inflater, container, false)
    ) {

        init {
            binding.lifecycleOwner = lifecycleOwner
            binding.postDetail = postDetailLive
            binding.actionListener = clickListener

            postDetailLive.observe(lifecycleOwner) { postDetail ->
                if (postDetail == null) return@observe
                changeButtonsVisibility(postDetail)
            }
        }

        private fun changeButtonsVisibility(postDetail: PostDetail) {
            with(binding) {
                val status = postDetail.status
                buttonAssignFixer.isGone = status != OPEN
                buttonReassignFixer.isGone = status != PENDING
                buttonClosePost.isGone = status in arrayOf(
                    ON_PROGRESS,
                    PENDING,
                    FINISHED
                )
                buttonCancelFixing.isGone = status != ON_PROGRESS
                buttonFinishJob.isGone = status !in arrayOf(
                    ON_PROGRESS,
                    FINISHED,
                )
            }
        }
    }

    class FixerActionBottomVH(
        container: ViewGroup,
        postDetailLive: LiveData<PostDetail>,
        lifecycleOwner: LifecycleOwner,
        clickListener: ClickListener,
    ) : BaseViewHolder<PostDetailsAdapterItem.ActionBottomItem, ItemDetailsPostActionsFixerBinding>(
        ItemDetailsPostActionsFixerBinding.inflate(container.inflater, container, false)
    ) {
        init {
            binding.lifecycleOwner = lifecycleOwner
            binding.clickListener = clickListener
            postDetailLive.observe(lifecycleOwner) { postDetail ->
                if (postDetail == null) return@observe
                changeButtonsAppearance(postDetail)
            }
        }

        private fun changeButtonsAppearance(postDetail: PostDetail) {
            val status = postDetail.status
            val context = binding.root.context
            val haveYouApplied = postDetail.appliedFixers.any { it.id == postDetail.loggedInUserId }
            val assignedToYou = postDetail.assignedFixerId == postDetail.loggedInUserId
            with(binding.buttonApplyJob) {
                isGone = status == FINISHED
                isEnabled = !haveYouApplied

                text = context.getString(
                    when {
                        status in listOf(NEW, OPEN) -> R.string.action_apply_job
                        assignedToYou -> R.string.label_job_assigned_to_you
                        else -> R.string.label_job_assigned_to_other
                    }
                )
            }
            with(binding.buttonStartFixing) {
                isGone = !(status == PENDING && assignedToYou)
                text = context.getString(
                    when (status) {
                        FINISHED -> R.string.label_job_finished
                        ON_PROGRESS -> R.string.label_job_on_progress
                        else -> R.string.action_start_fixing
                    }
                )
            }
        }
    }
}
