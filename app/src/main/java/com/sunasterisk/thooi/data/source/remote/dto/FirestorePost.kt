package com.sunasterisk.thooi.data.source.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.util.toGeoPoint
import com.sunasterisk.thooi.util.toTimeStamp

data class FirestorePost(
    val address: String = "",
    val appointment: Timestamp = Timestamp.now(),
    val category: String = "",
    val customer: String = "",
    val description: String = "",
    val fixer_id: String? = null,
    val fixers_id: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val suggestedPrice: Int = 0,
    val status: String = "",
    val voucher: String? = null
) {
    constructor() : this(
        "",
        Timestamp.now(),
        "",
        "",
        "",
        "",
        emptyList(),
        emptyList(),
        GeoPoint(0.0, 0.0),
        0,
        "",
        null
    )

    constructor(post: Post) : this(
        post.address,
        post.appointment.toTimeStamp(),
        post.categoryRef,
        post.customerRef,
        post.description,
        post.fixerId,
        post.appliedFixerIds,
        post.imagesRefs,
        post.location.toGeoPoint(),
        post.suggestedPrice,
        post.status.name,
        post.voucher
    )
}
