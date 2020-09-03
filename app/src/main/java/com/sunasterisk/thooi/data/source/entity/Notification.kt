package com.sunasterisk.thooi.data.source.entity

import org.threeten.bp.LocalDateTime

data class Notification(
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
