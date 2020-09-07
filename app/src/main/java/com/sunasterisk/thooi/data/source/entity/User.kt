package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.sunasterisk.thooi.data.source.entity.User.Companion.TABLE_NAME
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_ZONE_OFFSET
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreUser
import com.sunasterisk.thooi.util.toLatLng
import com.sunasterisk.thooi.util.toLocalDate
import com.sunasterisk.thooi.util.toLocalDateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(tableName = TABLE_NAME)
data class User(
    @PrimaryKey
    val id: String,
    val address: String,
    val bio: String,
    val createdDateTime: LocalDateTime,
    val dateOfBirth: LocalDate,
    val email: String,
    val fullName: String,
    val imageUrl: String,
    val location: LatLng,
    val organization: String,
    val phone: String,
    val professions: List<String>,
    val userType: UserType
) {

    constructor(id: String, firestoreUser: FirestoreUser) : this(
        id,
        firestoreUser.address,
        firestoreUser.bio,
        firestoreUser.created_at.toLocalDateTime(),
        firestoreUser.date_of_birth.toLocalDate(),
        firestoreUser.email,
        firestoreUser.full_name,
        firestoreUser.image_url,
        firestoreUser.location.toLatLng(),
        firestoreUser.organization,
        firestoreUser.phone,
        firestoreUser.professions,
        if (firestoreUser.type == USER_TYPE_CUSTOMER) UserType.CUSTOMER else UserType.FIXER
    )

    companion object {
        const val TABLE_NAME = "user"
        const val USER_TYPE_CUSTOMER = "CUSTOMER"

        val default = User(
            "",
            "",
            "",
            LocalDateTime.ofEpochSecond(0, 0, DEFAULT_ZONE_OFFSET),
            LocalDate.ofEpochDay(0),
            "",
            "",
            "",
            LatLng(0.0, 0.0),
            "",
            "",
            emptyList(),
            UserType.CUSTOMER
        )
    }
}

enum class UserType { CUSTOMER, FIXER }
