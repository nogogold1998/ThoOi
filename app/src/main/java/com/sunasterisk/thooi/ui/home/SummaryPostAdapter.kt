package com.sunasterisk.thooi.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.SummaryPost
import com.sunasterisk.thooi.databinding.ItemSummaryPostBinding
import com.sunasterisk.thooi.util.inflater

/**
 * Created by Cong Vu Chi on 10/09/20 09:52.
 */
class SummaryPostAdapter(
    private val itemClickListener: (SummaryPost) -> Unit,
) : ListAdapter<SummaryPost, SummaryPostVH>(SummaryPost.diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SummaryPostVH(parent, itemClickListener)

    override fun onBindViewHolder(holder: SummaryPostVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class SummaryPostVH(
    parent: ViewGroup,
    clickListener: (SummaryPost) -> Unit,
) : BaseViewHolder<SummaryPost, ItemSummaryPostBinding>(
    ItemSummaryPostBinding.inflate(parent.inflater, parent, false)
) {

    init {
        with(binding) {
            textPostSummaryAddress.isSelected = true
            textPostSummaryPrice.isSelected = true
            root.setOnClickListener {
                cachedValue?.let(clickListener)
            }
        }
    }

    override fun onBind(item: SummaryPost, binding: ItemSummaryPostBinding) = with(binding) {
        summaryPost = item
        executePendingBindings()
        cachedValue = item
    }
}
