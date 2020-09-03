package com.sunasterisk.thooi.data.source.entity

import org.threeten.bp.LocalDateTime

data class Message(
    val id: String,
    val createdAt: LocalDateTime,
    val senderRef: String,
    val receiverRef: String,
    val text: String
)
