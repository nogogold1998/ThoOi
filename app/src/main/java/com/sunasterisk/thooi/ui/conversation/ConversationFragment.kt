package com.sunasterisk.thooi.ui.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentConversationBinding
import org.koin.android.ext.android.inject

class ConversationFragment : BaseFragment<FragmentConversationBinding>() {

    private val args: ConversationFragmentArgs by navArgs()
    private val viewModel by inject<ConversationViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentConversationBinding.inflate(inflater, container, attachToRoot).also {
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupView() {
        viewModel.apply {
            FirebaseAuth.getInstance().uid?.let { setCurrentUser(it) }
            setProfileInfo()
            loadMessages(args.conversationId)
        }
    }
}
