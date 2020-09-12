package com.sunasterisk.thooi.ui.post.newpost

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentNewPostBinding
import org.koin.android.ext.android.inject

class NewPostFragment : BaseFragment<FragmentNewPostBinding>() {

    private val viewModel by inject<NewPostViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentNewPostBinding.inflate(inflater).also {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
    }


}
