package com.sunasterisk.thooi.ui.conversation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.Message
import com.sunasterisk.thooi.data.model.MessageType
import com.sunasterisk.thooi.databinding.ItemReceivedMessageBinding
import com.sunasterisk.thooi.databinding.ItemSentMessageBinding
import com.sunasterisk.thooi.util.inflater

class MessageAdapter : ListAdapter<Message, BaseViewHolder<Message, *>>(Message.diffUtil) {

    override fun getItemViewType(position: Int) =
        when (getItem(position).type) {
            MessageType.SENT_TEXT -> TYPE_SENT_TEXT
            MessageType.RECEIVED_TEXT -> TYPE_RECEIVED_TEXT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Message, *> =
        when (viewType) {
            TYPE_SENT_TEXT -> SentMessageViewHolder(parent)
            TYPE_RECEIVED_TEXT -> ReceivedMessageViewHolder(parent)
            else -> throw Exception("Message's type does not supported")

        }

    override fun onBindViewHolder(holder: BaseViewHolder<Message, *>, position: Int) {
        holder.bind(getItem(position))
    }

    class SentMessageViewHolder(container: ViewGroup) :
        BaseViewHolder<Message, ItemSentMessageBinding>(
            ItemSentMessageBinding.inflate(container.inflater, container, false)
        ) {
        override fun onBind(item: Message, binding: ItemSentMessageBinding) =
            with(binding) {
                message = item
                executePendingBindings()
            }

    }

    class ReceivedMessageViewHolder(container: ViewGroup) :
        BaseViewHolder<Message, ItemReceivedMessageBinding>(
            ItemReceivedMessageBinding.inflate(container.inflater, container, false)
        ) {
        override fun onBind(item: Message, binding: ItemReceivedMessageBinding) =
            with(binding) {
                message = item
                executePendingBindings()
            }
    }

    companion object {
        const val TYPE_SENT_TEXT = 1
        const val TYPE_RECEIVED_TEXT = 2
    }
}
