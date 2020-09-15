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
    val customer: SummaryUser = SummaryUser(),
    val description: String = "",
    val address: String = "",
    val appliedFixers: List<SummaryUser> = emptyList(),
    val status: PostStatus = PostStatus.NEW,
    val assignedFixerId: String? = null,
    val loggedInUserId: String = "",
    val suggestedPrice: String = "",
)

