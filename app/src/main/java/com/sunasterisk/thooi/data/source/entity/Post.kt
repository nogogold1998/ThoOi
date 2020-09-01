package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng
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
    val imagesRef: String,
    val location: LatLng,
    val suggestedPrice: String,
    val status: PostStatus,
    val voucher: String?
)

enum class PostStatus
