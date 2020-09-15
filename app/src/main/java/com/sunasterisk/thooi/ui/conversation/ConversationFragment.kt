package com.sunasterisk.thooi.ui.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentConversationBinding
import org.koin.android.ext.android.inject

class ConversationFragment : BaseFragment<FragmentConversationBinding>() {

    private val viewModel by inject<ConversationViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentConversationBinding.inflate(inflater, container, attachToRoot).also {
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }
}
