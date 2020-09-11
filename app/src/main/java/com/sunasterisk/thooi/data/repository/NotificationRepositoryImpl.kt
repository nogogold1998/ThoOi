package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.data.source.NotificationDataSource
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(
    private val local: NotificationDataSource.Local,
    private val remote: NotificationDataSource.Remote
) : NotificationRepository {

    override fun getAllNotifications(userId: String) = flow<List<Notification>> {
    }

    override fun getNotificationById(id: String) = flow<Notification> {
    }
}
