package com.sunasterisk.thooi.data.source.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class FirestoreNotification(
    val content: String,
    val created_at: Timestamp,
    val is_read: Boolean,
    val receiver: DocumentReference,
    val sender: DocumentReference,
    val type: String,
)
