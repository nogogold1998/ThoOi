package com.sunasterisk.thooi.ui.home.model

import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.sunasterisk.thooi.base.BaseAdapterItem
import com.sunasterisk.thooi.data.source.entity.Category

/**
 * Created by Cong Vu Chi on 10/09/20 08:42.
 */
sealed class CategoryAdapterItem<T : Any>(
    val viewType: CategoryAdapterViewType,
) : BaseAdapterItem<T>() {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryAdapterItem<*>>() {
            override fun areItemsTheSame(
                oldItem: CategoryAdapterItem<*>,
                newItem: CategoryAdapterItem<*>,
            ) = oldItem.isTheSame(newItem)

            override fun areContentsTheSame(
                oldItem: CategoryAdapterItem<*>,
                newItem: CategoryAdapterItem<*>,
            ) = oldItem.isContentTheSame(newItem)
        }
    }
}

class TitleTextDividerItem(
    override val data: Int,
) : CategoryAdapterItem<@StringRes Int>(CategoryAdapterViewType.TEXT_DIVIDER) {
    override fun isTheSame(other: BaseAdapterItem<*>) = other is TitleTextDividerItem

    override fun isContentTheSame(other: BaseAdapterItem<*>) =
        other is TitleTextDividerItem && data == other.data
}

class PostCategoryItem(
    override val data: Category,
) : CategoryAdapterItem<Category>(CategoryAdapterViewType.POST_CATEGORY) {
    override fun isTheSame(other: BaseAdapterItem<*>) =
        other is PostCategoryItem && data.id == other.data.id

    override fun isContentTheSame(other: BaseAdapterItem<*>) =
        other is PostCategoryItem && data == other.data
}
