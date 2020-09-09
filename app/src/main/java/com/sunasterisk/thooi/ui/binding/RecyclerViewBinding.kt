package com.sunasterisk.thooi.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.ui.post.detail.SummaryUserAdapter

/**
 * Created by Cong Vu Chi on 06/09/20 23:38.
 */
@BindingAdapter("listSummaryUser")
fun RecyclerView.bindSummaryUser(items: List<SummaryUser>?) {
    (adapter as? SummaryUserAdapter)?.submitList(items)
}
