package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_ZONE_OFFSET
import org.threeten.bp.LocalDateTime

@Entity
data class Post(
    @PrimaryKey
    val id: String,
    val address: String,
    val appointment: LocalDateTime,
    val categoryRef: String,
    val customerRef: String,
    val description: String,
    val fixerId: String?,
    val appliedFixerIds: List<String>,
    val imagesRefs: List<String>, //IF-CONFLICT: take this version
    val location: LatLng?,
    val suggestedPrice: String,
    val status: PostStatus,
    val voucher: String?,
) {

    companion object {
        val default = Post(
            "",
            "",
            LocalDateTime.ofEpochSecond(0, 0, DEFAULT_ZONE_OFFSET),
            "",
            "",
            "",
            null,
            emptyList(),
            emptyList(),
            null,
            "",
            PostStatus.NEW,
            null
        )
    }
}

enum class PostStatus {
    NEW, PENDING, FINISHED
}
