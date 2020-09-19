package com.sunasterisk.thooi.data.model

import androidx.recyclerview.widget.DiffUtil
import com.sunasterisk.thooi.data.source.entity.NotificationType
import org.threeten.bp.LocalDateTime

data class Notification(
    val id: String = "",
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isRead: Boolean = false,
    var senderId: String = "",
    val type: NotificationType = NotificationType.Notification,
    var imageUrl: String = ""
) {
    constructor(
        entityNotification: com.sunasterisk.thooi.data.source.entity.Notification
    ) : this(
        entityNotification.id,
        entityNotification.content,
        entityNotification.createdAt,
        entityNotification.isRead,
        entityNotification.senderRef,
        entityNotification.type,
        entityNotification.imageUrl
    )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification) =
                oldItem == newItem
        }
    }
}
