package com.sunasterisk.thooi.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

open class MarginItemDecoration(
    resources: Resources,
    @DimenRes verticalDimenRes: Int? = null,
    @DimenRes horizontalDimenRes: Int? = null,
) : RecyclerView.ItemDecoration() {
    private val spaceHeight = verticalDimenRes?.let(resources::getDimension)?.toInt() ?: 0
    private val spaceWidth = (horizontalDimenRes?.let(resources::getDimension)?.toInt() ?: 0) / 2

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State,
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) top = spaceHeight
            left = spaceWidth
            right = spaceWidth
            bottom = spaceHeight
        }
    }
}
