package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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
)

enum class NotificationType {
    Notification,
    Message
}
