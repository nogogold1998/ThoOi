package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng
import com.sunasterisk.thooi.data.source.entity.User.Companion.TABLE_NAME
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_ZONE_OFFSET
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
    val location: LatLng?, //FIXME: assign to ToanDV fix create default LatLng value
    val organization: String,
    val phone: String,
    val professions: List<String>,
    val userType: UserType,
) {
    companion object {
        const val TABLE_NAME = "user"

        val default = User("",
            "",
            "",
            LocalDateTime.ofEpochSecond(0, 0, DEFAULT_ZONE_OFFSET),
            LocalDate.ofEpochDay(0),
            "",
            "",
            null,
            "",
            "",
            emptyList(),
            UserType.CUSTOMER
        )
    }
}

enum class UserType { CUSTOMER, FIXER }
