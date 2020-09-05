package com.sunasterisk.thooi.data.source.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.util.toGeoPoint
import com.sunasterisk.thooi.util.toTimeStamp

data class FirestoreUser(
    val address: String,
    val bio: String,
    val conversations: List<DocumentReference>,
    val created_at: Timestamp,
    val date_of_birth: Timestamp,
    val email: String,
    val image_url: String,
    val full_name: String,
    val location: GeoPoint,
    val organization: String,
    val phone: String,
    val professions: List<String>,
    val type: String,
) {
    constructor() : this(
        "",
        "",
        listOf(),
        Timestamp.now(),
        Timestamp.now(),
        "",
        "",
        "",
        GeoPoint(0.0, 0.0),
        "",
        "",
        listOf(),
        ""
    )

    constructor(user: User) : this(
        user.address,
        user.bio,
        listOf<DocumentReference>(),
        user.createdDateTime.toTimeStamp(),
        user.dateOfBirth.toTimeStamp(),
        user.email,
        user.imageUrl,
        user.fullName,
        user.location.toGeoPoint(),
        user.organization,
        user.phone,
        user.professions,
        user.userType.name
    )
}
