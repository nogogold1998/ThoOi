package com.sunasterisk.thooi.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel by inject<SettingsViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentSettingsBinding.inflate(inflater).also {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
    }

    override fun initListener() {
        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(R.id.setting_to_editProfile)
        }
    }
}
