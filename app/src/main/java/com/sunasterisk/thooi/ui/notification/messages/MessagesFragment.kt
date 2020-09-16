package com.sunasterisk.thooi.ui.notification.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.data.model.Conversation
import com.sunasterisk.thooi.databinding.FragmentMessagesBinding
import com.sunasterisk.thooi.ui.notification.NotificationFragmentDirections
import com.sunasterisk.thooi.util.toast
import kotlinx.android.synthetic.main.fragment_messages.*
import org.koin.android.ext.android.inject

class MessagesFragment : BaseFragment<FragmentMessagesBinding>() {

    private val viewModel by inject<MessagesViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentMessagesBinding.inflate(inflater, container, attachToRoot).also {
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupView() {
        setUpRecyclerView()
    }

    override fun onObserveLiveData() {
        FirebaseAuth.getInstance().uid?.let { viewModel.setCurrentUser(it) }
        viewModel.getConversations()
        viewModel.conversations.observe(viewLifecycleOwner) {
            (recyclerConversations.adapter as ConversationAdapter).submitList(it)
        }
    }

    private fun setUpRecyclerView() = with(binding.recyclerConversations) {
        adapter = ConversationAdapter().also {
            it.onItemClick = { conversation -> goToConversationDetail(conversation) }
        }
    }

    private fun goToConversationDetail(conversation: Conversation) {
        context?.toast("Feature currently not working")
//        findNavController().navigate(NotificationFragmentDirections.notificationToChat(conversation.id))
    }
}
