package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng
import com.sunasterisk.thooi.data.source.entity.User.Companion.TABLE_NAME
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(tableName = TABLE_NAME)
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
) {
    companion object {
        const val TABLE_NAME = "user"

        const val COL_USER_TYPE_CUSTOMER = "customer"
        const val COL_USER_TYPE_FIXER = "fixer"
    }
}

enum class UserType(val value: String) {
    CUSTOMER(User.COL_USER_TYPE_CUSTOMER),
    FIXER(User.COL_USER_TYPE_FIXER)
}
