package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunasterisk.thooi.data.model.Message
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreMessage
import com.sunasterisk.thooi.util.toLocalDateTime
import org.threeten.bp.LocalDateTime

@Entity
data class Message(
    @PrimaryKey
    val id: String = "",
    val conversationId: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val senderRef: String = "",
    val receiverRef: String = "",
    val text: String = "",
    var imageUrl: String = "",
    val isRead: Boolean = false
) {
    constructor(id: String, firestoreMessage: FirestoreMessage) : this(
        id,
        firestoreMessage.conversationId,
        firestoreMessage.created_at.toLocalDateTime(),
        firestoreMessage.sender?.id ?: "",
        firestoreMessage.receiver?.id ?: "",
        firestoreMessage.text,
        firestoreMessage.image_url,
        firestoreMessage.is_read
    )

    constructor(modelMessage: Message) : this(
        modelMessage.id,
        modelMessage.conversationId,
        modelMessage.createdAt,
        modelMessage.senderId,
        modelMessage.receivedId,
        modelMessage.text,
        modelMessage.imageUrl,
        modelMessage.isRead
    )
}
