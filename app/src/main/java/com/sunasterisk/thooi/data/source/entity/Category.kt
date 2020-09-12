package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreCategory

@Entity
data class Category(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrls: List<String>,
    val span: Int = 1,
) {
    constructor(id: String, firestoreCategory: FirestoreCategory) : this(
        id,
        firestoreCategory.title,
        firestoreCategory.images,
        firestoreCategory.span.toIntOrNull() ?: 1,
    )
}
