package com.sunasterisk.thooi.data.source.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class FirestoreUser(
    val address: String = "",
    val bio: String = "",
    val conversations: List<DocumentReference> = emptyList(),
    val created_at: Timestamp = Timestamp.now(),
    val date_of_birth: Timestamp = Timestamp.now(),
    val email: String = "",
    val image_url: String = "",
    val full_name: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val organization: String = "",
    val phone: String = "",
    val professions: List<String> = emptyList(),
    val type: String = ""
)
