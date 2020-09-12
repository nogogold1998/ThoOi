package com.sunasterisk.thooi.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.databinding.FragmentCategoryBinding
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    private val userType: UserType by inject()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentCategoryBinding.inflate(inflater, container, attachToRoot)

    override fun setupView() {
    }
}
