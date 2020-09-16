package com.sunasterisk.thooi.data.source.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.NotificationDataSource
import com.sunasterisk.thooi.data.source.entity.Notification
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.FIELD_RECEIVER
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.NOTIFICATIONS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.USERS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreNotification
import com.sunasterisk.thooi.util.getSnapshotFlow
import com.sunasterisk.thooi.util.toObjectWithId
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class NotificationRemoteDataSource(
    auth: FirebaseAuth,
    database: FirebaseFirestore
) : NotificationDataSource.Remote {

    private val userId = auth.uid
    private val notificationCollection = database.collection(NOTIFICATIONS_COLLECTION)
    private val userCollection = database.collection(USERS_COLLECTION)

    override fun getAllNotification(): Flow<Result<List<Notification>>> = callbackFlow {

        offer(Result.loading())

        val listener =
            notificationCollection
                .whereEqualTo(
                    FIELD_RECEIVER, userId?.let { userCollection.document(it) }
                )
                .addSnapshotListener { snapshot, exception ->
                    val notifications = ArrayList<Notification>()
                    snapshot?.documents?.forEach { document ->
                        Log.d("AAA", document.toString())
                        document.toObjectWithId(
                            FirestoreNotification::class.java, Notification::class
                        )?.let {
                            Log.d("AAA", it.senderRef)
                            getUserImgUrl(it.senderRef) { url ->
                                it.imageUrl = url
                                Log.d("AAAAA", it.toString())
                            }
                            notifications.add(it)
                        }
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

    override fun getUserImgUrl(id: String, function: (String) -> Unit) {
        userCollection.document(id).get()
            .addOnSuccessListener {
                val url = it?.getString("image_url")
                url?.let { it1 -> function.invoke(it1) }
            }
    }

    override fun getNotificationById(id: String) =
        notificationCollection.document(id).getSnapshotFlow {
            it.toObjectWithId(FirestoreNotification::class.java, Notification::class)
        }
}
