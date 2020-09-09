package com.sunasterisk.thooi.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.thooi.ui.post.detail.PostDetailsAdapter
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterItem

/**
 * Created by Cong Vu Chi on 06/09/20 23:38.
 */
@BindingAdapter("listPostDetailsAdapterItem")
fun RecyclerView.bindSummaryUser(items: List<PostDetailsAdapterItem<*>>?) {
    (adapter as? PostDetailsAdapter)?.submitList(items)
}
