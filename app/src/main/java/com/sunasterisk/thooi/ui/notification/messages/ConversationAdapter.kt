package com.sunasterisk.thooi.ui.notification.messages

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.Conversation
import com.sunasterisk.thooi.databinding.ItemConversationBinding
import com.sunasterisk.thooi.util.inflater

class ConversationAdapter :
    ListAdapter<Conversation, ConversationAdapter.ConversationViewHolder>(Conversation.diffUtil) {

    var onItemClick: (Conversation) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConversationViewHolder(parent, onItemClick)

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ConversationViewHolder(
        container: ViewGroup,
        val onItemClick: (Conversation) -> Unit
    ) :
        BaseViewHolder<Conversation, ItemConversationBinding>(
            ItemConversationBinding.inflate(container.inflater, container, false)
        ) {

        override fun onBind(item: Conversation, binding: ItemConversationBinding) =
            with(binding) {
                root.setOnClickListener { onItemClick(item) }
                conversation = item
                executePendingBindings()
            }
    }
}
