package com.sunasterisk.thooi.service

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.getBitmapFromRes
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class CloudMessageService : FirebaseMessagingService() {

    private val userRepo: UserRepository by inject()
    private val notificationManager by lazy {
        getSystemService(NotificationManager::class.java) as NotificationManager
    }

    override fun onNewToken(token: String) {
        notificationManager.setupNotificationChannels(
            getString(R.string.default_notification_channel_id),
            getString(R.string.default_channel_name),
            getString(R.string.description_default_channel)
        )
        runBlocking { userRepo.setToken(token) }
    }

    override fun onMessageReceived(remoteMsg: RemoteMessage) {
        remoteMsg.notification?.let {
            val builder = NotificationCompat.Builder(
                applicationContext,
                getString(R.string.default_notification_channel_id)
            ).setSmallIcon(R.drawable.ic_tho_oi_small)
                .setLargeIcon(applicationContext.getBitmapFromRes(R.drawable.ic_round_notifications_active_24))
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(FCM_NOTIFICATION_ID, builder.build())
        }
    }

    private fun NotificationManager.setupNotificationChannels(
        channelId: String,
        channelName: String,
        description: String,
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                    setDescription(description)
                    enableVibration(true)
                }
            createNotificationChannel(channel)
        }
    }

    companion object {
        const val FCM_NOTIFICATION_ID = 98
    }
}
