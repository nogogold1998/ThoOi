package com.sunasterisk.thooi.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentProfileBinding
import com.sunasterisk.thooi.di.getViewModelFactory
import com.sunasterisk.thooi.util.toast

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val navArgs: ProfileFragmentArgs by navArgs()

    private val viewModel: ProfileVM by navGraphViewModels(R.id.nav_graph) { getViewModelFactory() }

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
        binding.buttonProfileCall.setOnClickListener {
            startActivity(Intent(Intent.ACTION_CALL,
                Uri.fromParts("tel", viewModel.user.value?.phone, null)))
        }
        binding.buttonProfileChat.setOnClickListener {
            context?.toast(R.string.msg_missing_feature)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUserById(navArgs.userId)
    }
}
