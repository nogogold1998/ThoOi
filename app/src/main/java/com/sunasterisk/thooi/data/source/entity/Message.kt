package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class Message(
    @PrimaryKey
    val id: String,
    val createdAt: LocalDateTime,
    val senderRef: String,
    val receiverRef: String,
    val text: String
)
