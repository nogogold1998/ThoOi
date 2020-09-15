package com.sunasterisk.thooi.data.model

import androidx.recyclerview.widget.DiffUtil

data class Conversation(
    val id: String = "",
    val lastMessage: String = "",
    val lastTime: String = "",
    val otherName: String = "",
    val otherImageProfile: String = ""
) {
    constructor(
        currentUserId: String,
        entityConversation: com.sunasterisk.thooi.data.source.entity.Conversation
    ) : this(
        entityConversation.id,
        entityConversation.lastMessage,
        entityConversation.lastTime,
        if (currentUserId == entityConversation.members[0].id) entityConversation.members[0].fullName else entityConversation.members[1].fullName,
        if (currentUserId == entityConversation.members[0].id) entityConversation.members[0].imageUrl else entityConversation.members[1].imageUrl
    ) {
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Conversation>() {
            override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation) =
                oldItem == newItem
        }
    }
}
