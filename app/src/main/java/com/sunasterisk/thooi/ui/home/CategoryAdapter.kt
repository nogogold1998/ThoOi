package com.sunasterisk.thooi.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.databinding.ItemCategoryPostBinding
import com.sunasterisk.thooi.databinding.ItemDividerTextBinding
import com.sunasterisk.thooi.ui.home.model.CategoryAdapterItem
import com.sunasterisk.thooi.ui.home.model.CategoryAdapterViewType
import com.sunasterisk.thooi.ui.home.model.CategoryAdapterViewType.POST_CATEGORY
import com.sunasterisk.thooi.ui.home.model.CategoryAdapterViewType.TEXT_DIVIDER
import com.sunasterisk.thooi.ui.home.model.PostCategoryItem
import com.sunasterisk.thooi.ui.home.model.TitleTextDividerItem
import com.sunasterisk.thooi.util.inflater

/**
 * Created by Cong Vu Chi on 10/09/20 08:37.
 */
class CategoryAdapter : ListAdapter<CategoryAdapterItem<*>, BaseViewHolder<*, *>>(
    CategoryAdapterItem.diffUtil
) {
    override fun getItemViewType(position: Int) = getItem(position).viewType.layoutRes

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<*, *> {
        val adapterViewType = CategoryAdapterViewType.values()
            .firstOrNull { it.layoutRes == viewType }
        return when (adapterViewType) {
            TEXT_DIVIDER -> TitleTextVH(parent)
            POST_CATEGORY -> PostCategoryVH(parent)
            null -> throw IllegalArgumentException("Cannot find a match for given viewType, was: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as? BaseViewHolder<CategoryAdapterItem<*>, *>)?.bind(getItem(position))
    }

    fun getSpanSize(position: Int, maxSpanSize: Int = 3) = when (val item = getItem(position)) {
        is TitleTextDividerItem -> maxSpanSize
        is PostCategoryItem -> item.data.span
    }
}

class TitleTextVH(
    parent: ViewGroup,
) : BaseViewHolder<TitleTextDividerItem, ItemDividerTextBinding>(
    ItemDividerTextBinding.inflate(parent.inflater, parent, false)
) {
    override fun onBind(item: TitleTextDividerItem, binding: ItemDividerTextBinding) {
        binding.textDividerCategory.run {
            text = resources.getString(item.data)
        }
    }
}

class PostCategoryVH(
    parent: ViewGroup,
) : BaseViewHolder<PostCategoryItem, ItemCategoryPostBinding>(
    ItemCategoryPostBinding.inflate(parent.inflater, parent, false)
) {
    override fun onBind(item: PostCategoryItem, binding: ItemCategoryPostBinding) {
        binding.textTitleCategory.text = item.data.title
    }
}
