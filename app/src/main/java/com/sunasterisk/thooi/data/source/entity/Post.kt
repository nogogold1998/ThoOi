package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.sunasterisk.thooi.data.source.remote.dto.FirestorePost
import com.sunasterisk.thooi.util.toLatLng
import com.sunasterisk.thooi.util.toLocalDateTime
import org.threeten.bp.LocalDateTime

@Entity
data class Post(
    @PrimaryKey
    val id: String = "",
    val address: String = "",
    val appointment: LocalDateTime = LocalDateTime.now(),
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    val categoryRef: String = "",
    val customerRef: String = "",
    val description: String = "",
    val fixerId: String? = "",
    val appliedFixerIds: List<String> = emptyList(),
    val imagesRefs: List<String> = emptyList(),
    val location: LatLng = LatLng(0.0, 0.0),
    val suggestedPrice: String = "",
    val status: PostStatus = PostStatus.NEW,
    val voucher: String? = "",
    val title: String = "",
) {
    constructor(id: String, firestorePost: FirestorePost) : this(
        id,
        firestorePost.address,
        firestorePost.appointment.toLocalDateTime(),
        firestorePost.createdDateTime.toLocalDateTime(),
        firestorePost.category,
        firestorePost.customer,
        firestorePost.description,
        firestorePost.fixer_id,
        firestorePost.fixers_id,
        firestorePost.images,
        firestorePost.location.toLatLng(),
        firestorePost.suggestedPrice,
        PostStatus.valueOf(firestorePost.status),
        firestorePost.voucher,
        firestorePost.title
    )
}

enum class PostStatus {
    /**
     * No fixer has applied
     */
    NEW,

    /**
     * At least one fixer applied
     */
    OPEN,

    /**
     * Customer picked a fixer for the job
     */
    PENDING,

    /**
     * Fixer is doing her/his job
     */
    ON_PROGRESS,

    /**
     * Job has finished
     */
    FINISHED;

    fun nextStatus() = when (this) {
        NEW -> OPEN
        OPEN -> PENDING
        PENDING -> ON_PROGRESS
        ON_PROGRESS -> FINISHED
        FINISHED -> FINISHED
    }

    fun previousStatus() = when (this) {
        NEW -> NEW
        OPEN -> NEW
        PENDING -> OPEN
        ON_PROGRESS -> PENDING
        FINISHED -> FINISHED
    }
}
