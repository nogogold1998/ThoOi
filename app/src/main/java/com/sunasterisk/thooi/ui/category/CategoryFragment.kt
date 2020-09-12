package com.sunasterisk.thooi.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.databinding.FragmentCategoryBinding
import com.sunasterisk.thooi.util.MarginItemDecoration
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    private val userType: UserType by inject()

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
        title = "Hello world"
        setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecycler() = with(binding.recyclerCategory) {
        val categoryAdapter = CategoryAdapter {
            when (it) {
                is CategoryAdapterItem.PostItem -> TODO()
                is CategoryAdapterItem.FixerItem -> TODO()
            }
        }
        adapter = categoryAdapter
        addItemDecoration(MarginItemDecoration(resources, R.dimen.dp_8, R.dimen.dp_8))
    }
}
