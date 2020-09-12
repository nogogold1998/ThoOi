package com.sunasterisk.thooi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentHomeBinding
import com.sunasterisk.thooi.di.getViewModelFactory
import com.sunasterisk.thooi.ui.home.model.HomeNavigationEvent
import com.sunasterisk.thooi.ui.main.MainVM
import com.sunasterisk.thooi.util.MarginItemDecoration
import com.sunasterisk.thooi.util.observeEvent
import com.sunasterisk.thooi.util.verticalScrollProgress

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentHomeBinding.inflate(inflater, container, attachToRoot)

    private val viewModel: HomeVM by viewModels { getViewModelFactory() }

    private val mainVM: MainVM by activityViewModels { getViewModelFactory() }

    private var categoryAdapter: CategoryAdapter? = null

    private var summaryAdapter: SummaryPostAdapter? = null

    private var concatAdapter: ConcatAdapter? = null

    override fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categoryAdapter = CategoryAdapter {
            findNavController().navigate(HomeFragmentDirections.homeToCategory("asdas"))
        }
        val summaryPostAdapter = SummaryPostAdapter {
            viewModel.navigateToPost(it.id)
        }
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
            it.setOnScrollChangeListener { _, _, _, _, _ ->
                if (it.verticalScrollProgress == 1f) {
                    mainVM.collapseToolbar()
                } else {
                    mainVM.showToolbar()
                }
            }
        }
        this.categoryAdapter = categoryAdapter
        this.summaryAdapter = summaryPostAdapter
        this.concatAdapter = concatAdapter
    }

    override fun onObserveLiveData() {
        viewModel.categoryAdapterItems.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            categoryAdapter?.submitList(it)
        }
        viewModel.summaryPostAdapterItems.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            summaryAdapter?.submitList(it)
        }
        viewModel.navigationEvent.observe(this) { event ->
            if (event == null) return@observe
            when (event) {
                is HomeNavigationEvent.ToPostDetailEvent -> {
                    event.getContentIfNotHandled()
                        ?.let(HomeFragmentDirections::homeToPostDetails)
                        ?.let(findNavController()::navigate)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVM.loginUserId.observeEvent(viewLifecycleOwner) {
            if (it == null) return@observeEvent
            viewModel.loadDataByUserId(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryAdapter = null
        summaryAdapter = null
        concatAdapter = null
    }

    companion object {
        const val SPAN_COUNT_CATEGORY = 3
        const val SPAN_COUNT_SUMMARY_POST = 2
    }
}
