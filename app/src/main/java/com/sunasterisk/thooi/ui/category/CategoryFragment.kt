package com.sunasterisk.thooi.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentCategoryBinding
import com.sunasterisk.thooi.di.getViewModelFactory
import com.sunasterisk.thooi.util.MarginItemDecoration

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    private val viewModel: CategoryVM by navGraphViewModels(R.id.nav_graph) { getViewModelFactory() }

    private val navArgs: CategoryFragmentArgs by navArgs()

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
            when (it) {
                is CategoryAdapterItem.PostItem -> findNavController().navigate(
                    CategoryFragmentDirections.fixerCategoryToPostDetails(it.data.id)
                )
                is CategoryAdapterItem.FixerItem -> findNavController().navigate(
                    CategoryFragmentDirections.customerCategoryToProfile(it.data.id)
                )
            }
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentCategoryBinding.inflate(inflater, container, attachToRoot)

    override fun setupView() {
        setupToolbar()
        setupRecycler()
    }

    private fun setupToolbar() = with(binding.layoutToolbar) {
        viewModel.requestCategory(navArgs.categoryId)
        setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecycler() = with(binding.recyclerCategory) {
        adapter = categoryAdapter
        addItemDecoration(MarginItemDecoration(resources, R.dimen.dp_8, R.dimen.dp_16))
    }

    override fun onObserveLiveData() {
        viewModel.adapterItems.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            categoryAdapter.submitList(it)
        }
        viewModel.category.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.layoutToolbar.title = it.title
        }
    }
}
