package com.sunasterisk.thooi.data.model

import androidx.recyclerview.widget.DiffUtil
import org.threeten.bp.LocalDateTime

data class Message(
    val id: String = "",
    val conversationId: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val type: MessageType,
    val text: String = "",
    val senderId: String = "",
    val receivedId: String = "",
    val imageUrl: String = "",
    val senderImageUrl: String = "",
    val isRead: Boolean = false
) {
    constructor(
        currentUserId: String,
        senderImageUrl: String,
        entityMessage: com.sunasterisk.thooi.data.source.entity.Message
    ) : this(
        entityMessage.id,
        entityMessage.conversationId,
        entityMessage.createdAt,
        if (currentUserId == entityMessage.senderRef) MessageType.SENT_TEXT else MessageType.RECEIVED_TEXT,
        entityMessage.text,
        entityMessage.senderRef,
        entityMessage.receiverRef,
        entityMessage.imageUrl,
        senderImageUrl,
        entityMessage.isRead
    )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Message, newItem: Message) =
                oldItem == newItem
        }
    }
}

enum class MessageType {
    SENT_TEXT,
    RECEIVED_TEXT
}
