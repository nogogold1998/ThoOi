package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<Result<List<Notification>>>

    fun getNotificationById(id: String): Flow<Result<Notification>>

    fun getUserImgUrl(id: String, function: (String) -> Unit)
}
