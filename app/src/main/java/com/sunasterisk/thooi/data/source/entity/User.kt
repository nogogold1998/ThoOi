package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity
data class User(
    @PrimaryKey
    val id: String,
    val createdDateTime: LocalDateTime,
    val dateOfBirth: LocalDate,
    val email: String,
    val fullName: String,
    val location: LatLng,
    val organization: String,
    val phone: String,
    val professions: List<String>,
    val userType: UserType,
)

enum class UserType() {
    CUSTOMER,
    FIXER
}
