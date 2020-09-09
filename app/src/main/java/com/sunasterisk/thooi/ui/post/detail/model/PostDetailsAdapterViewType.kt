package com.sunasterisk.thooi.ui.post.detail.model

import androidx.annotation.LayoutRes
import com.sunasterisk.thooi.R

enum class PostDetailsAdapterViewType(
    @LayoutRes val layoutRes: Int,
) {
    POST_DETAILS(R.layout.item_details_post),

    SUMMARY_USER(R.layout.item_summary_user),

    ACTION_BOTTOM(R.layout.item_details_post_actions_customer)
}
