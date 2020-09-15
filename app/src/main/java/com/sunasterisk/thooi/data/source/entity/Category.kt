package com.sunasterisk.thooi.data.source.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreCategory
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Category(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrls: List<String>,
    val span: Int = 1,
) : Parcelable {
    constructor(id: String, firestoreCategory: FirestoreCategory) : this(
        id,
        firestoreCategory.title,
        firestoreCategory.images,
        firestoreCategory.span.toIntOrNull() ?: 1,
    )
}
