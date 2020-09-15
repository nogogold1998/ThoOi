package com.sunasterisk.thooi.ui.post.newpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.databinding.BottomSheetCategoryBinding
import com.sunasterisk.thooi.util.toast
import org.koin.android.ext.android.inject

class CategoryBottomSheet : BottomSheetDialogFragment() {
    private val viewModel by inject<CategoryViewModel>()
    private val adapter by lazy { CategoryAdapter() }
    private lateinit var binding: BottomSheetCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetCategoryBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = adapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            context?.toast(R.string.msg_unknown_error)
            dismiss()
        }
        adapter.setOnclickListener {
            setFragmentResult(RESULT_CATEGORY, bundleOf(RESULT_CATEGORY to it))
            dismiss()
        }
    }

    companion object {
        const val RESULT_CATEGORY = "result_category"
    }
}
