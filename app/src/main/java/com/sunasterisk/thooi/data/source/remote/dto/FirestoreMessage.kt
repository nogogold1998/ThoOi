package com.sunasterisk.thooi.data.source.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.source.entity.Message
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.USERS_COLLECTION
import com.sunasterisk.thooi.util.toTimeStamp

class FirestoreMessage(
    val created_at: Timestamp = Timestamp.now(),
    val sender: DocumentReference?,
    val receiver: DocumentReference?,
    val text: String = "",
    val image_url: String = "",
    val is_read: Boolean = false
) {
    constructor() : this(
        Timestamp.now(),
        null,
        null,
        "",
        "",
        false
    )

    constructor(database: FirebaseFirestore, message: Message) : this(
        message.createdAt.toTimeStamp(),
        database.collection(USERS_COLLECTION).document(message.senderRef),
        database.collection(USERS_COLLECTION).document(message.receiverRef),
        message.text,
        message.imageUrl
    )
}
