package com.sunasterisk.thooi.data.model

import org.threeten.bp.LocalDateTime

data class SummaryUser(
    val id: String = "",
    val avatarUrl: String = "",
    val fullName: String = "",
    val lastActive: LocalDateTime = LocalDateTime.now(),
)
