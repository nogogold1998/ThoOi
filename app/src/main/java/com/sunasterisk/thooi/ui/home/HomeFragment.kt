package com.sunasterisk.thooi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentHomeBinding.inflate(inflater, container, attachToRoot)

    private val viewModel: HomeVM by navGraphViewModels(R.id.nav_graph) { getViewModelFactory() }

    private val mainVM: MainVM by activityViewModels { getViewModelFactory() }

    private var categoryAdapter: CategoryAdapter? = null

    private var summaryAdapter: SummaryPostAdapter? = null

    override fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categoryAdapter = CategoryAdapter {
            findNavController().navigate(HomeFragmentDirections.homeToCategory(it.id))
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
        }
        this.categoryAdapter = categoryAdapter
        this.summaryAdapter = summaryPostAdapter
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
        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            if (event == null) return@observe
            when (event) {
                is HomeNavigationEvent.ToPostDetailEvent -> {
                    event.getContentIfNotHandled()
                        ?.let(HomeFragmentDirections::homeToPostDetails)
                        ?.let(findNavController()::navigate)
                }
            }
        }
        mainVM.scrollToTopEvent.observeEvent(viewLifecycleOwner) {
            binding.recyclerHome.smoothScrollToPosition(0)
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
    }

    companion object {
        const val SPAN_COUNT_CATEGORY = 3
        const val SPAN_COUNT_SUMMARY_POST = 2
    }
}
