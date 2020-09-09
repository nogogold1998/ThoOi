package com.sunasterisk.thooi.ui.post.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.databinding.ItemSummaryUserBinding
import com.sunasterisk.thooi.ui.post.detail.SummaryUserAdapter.UserVH
import com.sunasterisk.thooi.util.inflater

/**
 * Created by Cong Vu Chi on 06/09/20 23:08.
 */
class SummaryUserAdapter : ListAdapter<SummaryUser, UserVH>(SummaryUser.diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserVH(parent)

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(getItem(position))
    }

    class UserVH(container: ViewGroup) : BaseViewHolder<SummaryUser, ItemSummaryUserBinding>(
        ItemSummaryUserBinding.inflate(container.inflater, container, false)
    ) {

        // FIXME: RecyclerView inside NestedScrollView causes RecyclerVew to inflate all elements
        // NOTE: https://www.reddit.com/r/androiddev/comments/d8gi9v/recyclerview_inside_nestedscrollview_causes/f1blxes/
        override fun onBind(item: SummaryUser, binding: ItemSummaryUserBinding) =
            with(binding.layoutSummaryUser) {
                user = item
                executePendingBindings()
            }
    }
}
