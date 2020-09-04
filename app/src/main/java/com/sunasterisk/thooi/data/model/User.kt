package com.sunasterisk.thooi.data.model

import org.threeten.bp.LocalDateTime

data class User(
    val id: String,
    val avatarUrl: String,
    val fullName: String,
    val lastActive: LocalDateTime,
)
