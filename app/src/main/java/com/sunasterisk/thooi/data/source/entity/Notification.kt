package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreNotification
import com.sunasterisk.thooi.util.toLocalDateTime
import org.threeten.bp.LocalDateTime

@Entity
data class Notification(
    @PrimaryKey
    val id: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
    val receiverRef: String,
    val senderRef: String,
    val type: NotificationType,
) {
    constructor(id: String, firestoreNotification: FirestoreNotification) : this(
        id,
        firestoreNotification.content,
        firestoreNotification.created_at.toLocalDateTime(),
        firestoreNotification.is_read,
        firestoreNotification.receiver.id,
        firestoreNotification.sender.id,
        NotificationType.valueOf(firestoreNotification.type)
    )
}

enum class NotificationType {
    Notification,
    Message
}
