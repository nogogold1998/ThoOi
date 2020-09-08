package com.sunasterisk.thooi.data.model

import com.sunasterisk.thooi.data.source.entity.PostStatus
import org.threeten.bp.LocalDateTime

/**
 * Created by Cong Vu Chi on 03/09/20 14:50.
 */
data class PostDetail(
    val id: String = "",
    val thumbnailUrls: List<String> = emptyList(),
    val title: String = "",
    val postedDateTime: LocalDateTime = LocalDateTime.now(),
    val customer: User = User(),
    val description: String = "",
    val location: String = "",
    val appliedFixers: List<User> = emptyList(),
    val status: PostStatus = PostStatus.NEW,
)

