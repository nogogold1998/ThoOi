package com.sunasterisk.thooi.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentHomeBinding
import com.sunasterisk.thooi.util.MarginItemDecoration

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentHomeBinding.inflate(inflater, container, attachToRoot)

    override fun setupView() {
        // NOT-FINAL
        val categoryAdapter = CategoryAdapter()
        val summaryPostAdapter = SummaryPostAdapter()
        val concatAdapter = ConcatAdapter(categoryAdapter, summaryPostAdapter)
        binding.recyclerHome.let {
            it.adapter = concatAdapter
            val spanCount = SPAN_COUNT_CATEGORY * SPAN_COUNT_SUMMARY_POST
            it.layoutManager = GridLayoutManager(requireContext(), spanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        if (position - categoryAdapter.itemCount < 0) {
                            spanCount / SPAN_COUNT_CATEGORY * categoryAdapter.getSpanSize(position)
                        } else {
                            spanCount / SPAN_COUNT_SUMMARY_POST
                        }
                }
            }
            it.addItemDecoration(MarginItemDecoration(resources, R.dimen.dp_8, R.dimen.dp_8))
        }
    }

    companion object {
        const val SPAN_COUNT_CATEGORY = 3
        const val SPAN_COUNT_SUMMARY_POST = 2
    }
}
