package com.sunasterisk.thooi.ui.home.model

import androidx.annotation.LayoutRes
import com.sunasterisk.thooi.R

/**
 * Created by Cong Vu Chi on 10/09/20 08:38.
 */
enum class CategoryAdapterViewType(@LayoutRes val layoutRes: Int) {
    TEXT_DIVIDER(R.layout.item_divider_text),
    POST_CATEGORY(R.layout.item_category_post)
}
