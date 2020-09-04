package com.sunasterisk.thooi.data.model

import com.sunasterisk.thooi.data.source.entity.PostStatus
import org.threeten.bp.LocalDateTime

/**
 * Created by Cong Vu Chi on 03/09/20 14:50.
 */
data class PostDetail(
    val id: String,
    val thumbnailUrls: List<String>,
    val title: String,
    val postedDateTime: LocalDateTime,
    val customer: User,
    val description: String,
    val location: String,
    val appliedFixers: List<User>,
    val status: PostStatus,
)

