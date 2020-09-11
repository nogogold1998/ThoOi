package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(userId: String): Flow<List<Notification>>

    fun getNotificationById(id: String): Flow<Notification>
}
