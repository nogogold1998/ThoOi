package com.sunasterisk.thooi.data.repository

import android.util.Log
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.data.source.NotificationDataSource
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.util.mapResult
import com.sunasterisk.thooi.util.mapResultList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

class NotificationRepositoryImpl(
    private val local: NotificationDataSource.Local,
    private val remote: NotificationDataSource.Remote,
) : NotificationRepository {

    private val notificationsFlow by lazy {
        remote.getAllNotification().mapResultList { notification ->
            Notification(notification)
        }
    }

    override fun getAllNotifications() = notificationsFlow

    override fun getNotificationById(id: String) = flow<Result<Notification>> {
        remote.getNotificationById(id).mapResult { notification ->
            Notification(notification)
        }
    }

    override fun getUserImgUrl(id: String, function: (String) -> Unit) {
        remote.getUserImgUrl(id){
            function.invoke(it)
        }
    }
}
