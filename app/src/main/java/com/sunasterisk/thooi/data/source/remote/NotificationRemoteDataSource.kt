package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.NotificationDataSource
import com.sunasterisk.thooi.data.source.entity.Notification
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.FIELD_RECEIVER
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.NOTIFICATIONS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreNotification
import com.sunasterisk.thooi.util.getSnapshotFlow
import com.sunasterisk.thooi.util.toObjectWithId
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class NotificationRemoteDataSource(
    user: FirebaseUser,
    database: FirebaseFirestore
) : NotificationDataSource.Remote {

    private val userId = user.uid
    private val notificationCollection = database.collection(NOTIFICATIONS_COLLECTION)

    override fun getAllNotification(): Flow<Result<List<Notification>>> = callbackFlow {

        offer(Result.loading())

        val listener =
            notificationCollection
                .whereEqualTo(
                    FIELD_RECEIVER, notificationCollection.document(userId)
                )
                .addSnapshotListener { snapshot, exception ->
                    val notifications = ArrayList<Notification>()
                    snapshot?.documents?.forEach { document ->
                        document.toObjectWithId(
                            FirestoreNotification::class.java, Notification::class
                        )?.let { notifications.add(it) }
                    }
                    try {
                        offer(Result.success(notifications))
                    } catch (e: CancellationException) {
                        offer(Result.failed(e))
                    }

                    exception?.let {
                        offer(Result.failed(it))
                        cancel(it.message.toString())
                    }
                }

        awaitClose {
            listener.remove()
            cancel()
        }
    }

    override fun getNotificationById(id: String) =
        notificationCollection.document(id).getSnapshotFlow {
            it.toObjectWithId(FirestoreNotification::class.java, Notification::class)
        }
}
