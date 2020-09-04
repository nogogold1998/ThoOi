package com.sunasterisk.thooi.ui.post.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentDetailPostBinding

class PostDetailsFragment : BaseFragment<FragmentDetailPostBinding>() {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentDetailPostBinding.inflate(inflater, container, attachToRoot)
}
