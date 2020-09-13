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
        "https://www.w3schools.com/w3images/avatar2.png",
        user.fullName,
        user.createdDateTime
    )
}
