package com.sunasterisk.thooi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentProfileBinding
import com.sunasterisk.thooi.di.getViewModelFactory
import com.sunasterisk.thooi.ui.main.MainVM
import com.sunasterisk.thooi.util.verticalScrollProgress

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val navArgs: ProfileFragmentArgs by navArgs()

    private val viewModel: ProfileVM by navGraphViewModels(R.id.nav_graph) { getViewModelFactory() }

    private val mainVM: MainVM by activityViewModels { getViewModelFactory() }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentProfileBinding.inflate(inflater, container, attachToRoot).also {
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun initListener() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.scrollviewProfile.setOnScrollChangeListener { _, _, _, _, _ ->
            when (binding.scrollviewProfile.verticalScrollProgress) {
                1f -> mainVM.collapseToolbar()
                in 0f..0.8f -> mainVM.showToolbar()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUserById(navArgs.userId)
    }
}
