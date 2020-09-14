package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.sunasterisk.thooi.data.source.entity.User.Companion.TABLE_NAME
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreUser
import com.sunasterisk.thooi.util.toLatLng
import com.sunasterisk.thooi.util.toLocalDate
import com.sunasterisk.thooi.util.toLocalDateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

@Entity(tableName = TABLE_NAME)
data class User(
    @PrimaryKey
    val id: String = "",
    val address: String = "",
    val bio: String = "",
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    val dateOfBirth: LocalDate = LocalDate.now(),
    val email: String,
    val fullName: String,
    val imageUrl: String = "",
    val location: LatLng = LatLng(0.0, 0.0),
    val organization: String = "",
    val phone: String,
    val professions: List<String> = emptyList(),
    val userType: UserType,
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
        UserType.valueOf(firestoreUser.type)
    )

    companion object {
        const val TABLE_NAME = "user"
        val defaultZoneOffset: ZoneOffset = ZoneOffset.UTC
    }
}

enum class UserType {
    CUSTOMER,
    FIXER
}
