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
    var id: String = "",
    var address: String = "",
    var bio: String = "",
    var createdDateTime: LocalDateTime = LocalDateTime.now(),
    var dateOfBirth: LocalDate = LocalDate.now(),
    var email: String,
    var fullName: String,
    var imageUrl: String = "https://firebasestorage.googleapis.com/v0/b/tho-oi.appspot.com/o/avatar%2Favatar.png?alt=media&token=bc002db7-3c76-4509-b7c8-f41f9372ccc3",
    var location: LatLng = LatLng(0.0, 0.0),
    var organization: String = "",
    var phone: String,
    var professions: List<String> = emptyList(),
    var userType: UserType,
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
