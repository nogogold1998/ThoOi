package com.sunasterisk.thooi.ui.category

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.databinding.ItemCategoryJobBinding
import com.sunasterisk.thooi.databinding.ItemSummaryUserBinding
import com.sunasterisk.thooi.ui.category.CategoryAdapterViewType.FIXER
import com.sunasterisk.thooi.ui.category.CategoryAdapterViewType.POST
import com.sunasterisk.thooi.util.inflater

class CategoryAdapter(
    private val itemClickListener: (CategoryAdapterItem<*>) -> Unit,
) : ListAdapter<CategoryAdapterItem<*>, BaseViewHolder<*, *>>(CategoryAdapterItem.diffUtil) {

    override fun getItemViewType(position: Int) = getItem(position).viewType.layoutRes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> =
        when (viewType) {
            POST.layoutRes -> PostCategoryVH(parent, itemClickListener)
            FIXER.layoutRes -> FixerCategoryVH(parent, itemClickListener)
            else -> throw IllegalArgumentException("Cannot find a match for viewType: $viewType")
        }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as? BaseViewHolder<CategoryAdapterItem<*>, *>)?.bind(getItem(position))
    }
}

enum class CategoryAdapterViewType(@LayoutRes val layoutRes: Int) {
    POST(R.layout.item_category_post), FIXER(R.layout.item_summary_user)
}

sealed class CategoryAdapterItem<T : Any>(val viewType: CategoryAdapterViewType) {
    abstract val data: T
    abstract fun isTheSame(other: CategoryAdapterItem<*>): Boolean
    abstract fun isContentTheSame(other: CategoryAdapterItem<*>): Boolean

    data class PostItem(
        override val data: Post,
    ) : CategoryAdapterItem<Post>(POST) {
        override fun isTheSame(other: CategoryAdapterItem<*>) =
            other is PostItem && data.id == other.data.id

        override fun isContentTheSame(other: CategoryAdapterItem<*>) =
            other is PostItem && data == other.data
    }

    data class FixerItem(
        override val data: SummaryUser,
    ) : CategoryAdapterItem<SummaryUser>(FIXER) {
        override fun isTheSame(other: CategoryAdapterItem<*>) =
            other is FixerItem && data.id == other.data.id

        override fun isContentTheSame(other: CategoryAdapterItem<*>) =
            other is FixerItem && data == other.data
    }

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

class PostCategoryVH(
    parent: ViewGroup,
    itemClickListener: (CategoryAdapterItem<*>) -> Unit,
) : BaseViewHolder<CategoryAdapterItem.PostItem, ItemCategoryJobBinding>(
    ItemCategoryJobBinding.inflate(parent.inflater, parent, false)
) {

    init {
        binding.root.setOnClickListener { cachedValue?.let(itemClickListener) }
        binding.textPostSummaryPrice.isSelected = true
        binding.textPostSummaryAddress.isSelected = true
    }

    override fun onBind(item: CategoryAdapterItem.PostItem, binding: ItemCategoryJobBinding) =
        with(binding) {
            val post = item.data
            textTitlePost.text = post.title
            textDescriptionPost.text = post.description
            textPostSummaryAddress.text = post.address
            textPostSummaryPrice.text = post.suggestedPrice
        }
}

class FixerCategoryVH(
    parent: ViewGroup,
    itemClickListener: (CategoryAdapterItem<*>) -> Unit,
) : BaseViewHolder<CategoryAdapterItem.FixerItem, ItemSummaryUserBinding>(
    ItemSummaryUserBinding.inflate(parent.inflater, parent, false)
) {
    init {
        binding.root.setOnClickListener { cachedValue?.let(itemClickListener) }
        binding.layoutSummaryUser.textUserFullName.isSelected = true
    }

    override fun onBind(
        item: CategoryAdapterItem.FixerItem,
        binding: ItemSummaryUserBinding,
    ) = with(binding.layoutSummaryUser) {
        user = item.data
        executePendingBindings()
    }
}
