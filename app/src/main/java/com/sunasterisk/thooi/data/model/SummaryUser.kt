package com.sunasterisk.thooi.data.model

import com.sunasterisk.thooi.data.source.entity.User
import org.threeten.bp.LocalDateTime

data class SummaryUser(
    val id: String = "",
    val avatarUrl: String = "",
    val fullName: String = "",
    val lastActive: LocalDateTime = LocalDateTime.now(),
) {
    constructor(user: User) : this(
        user.id,
        user.imageUrl,
        user.fullName,
        user.createdDateTime
    )
}
