package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.data.source.NotificationDataSource
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.util.mapResult
import com.sunasterisk.thooi.util.mapResultList
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(
    private val userLocalDataSource: UserDataSource.Local,
    private val local: NotificationDataSource.Local,
    private val remote: NotificationDataSource.Remote,
) : NotificationRepository {

    override fun getAllNotifications() = flow<Result<List<Notification>>> {
        remote.getAllNotification()
            .mapResultList { notification ->
                Notification(notification).also {
                    userLocalDataSource.getUserBlocking(it.senderProfileImageUrl)?.imageUrl?.let { url ->
                        it.senderProfileImageUrl = url
                    }
                }
            }
    }

    override fun getNotificationById(id: String) = flow<Result<Notification>> {
        remote.getNotificationById(id).mapResult { notification ->
            Notification(notification).also {
                userLocalDataSource.getUserBlocking(it.senderProfileImageUrl)?.imageUrl?.let { url ->
                    it.senderProfileImageUrl = url
                }
            }
        }
    }
}
