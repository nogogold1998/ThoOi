package com.sunasterisk.thooi.data.source

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationDataSource {
    interface Local {

    }

    interface Remote {
        fun getAllNotification(): Flow<Result<List<Notification>>>

        fun getNotificationById(id: String): Flow<Result<Notification>>
    }
}
