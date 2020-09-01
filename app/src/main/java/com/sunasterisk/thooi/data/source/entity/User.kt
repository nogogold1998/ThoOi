package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng
import com.sunasterisk.thooi.data.source.entity.User.Companion.TABLE_NAME
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

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

        const val COL_USER_TYPE_CUSTOMER = "customer"
        const val COL_USER_TYPE_FIXER = "fixer"

        val defaultZoneOffset: ZoneOffset = ZoneOffset.UTC

        val default = User("",
            "",
            "",
            LocalDateTime.ofEpochSecond(0, 0, defaultZoneOffset),
            LocalDate.ofEpochDay(0),
            "",
            "",
            null,
            "",
            "",
            emptyList(),
            UserType.CUSTOMER)
    }
}

enum class UserType(val value: String) {
    CUSTOMER(User.COL_USER_TYPE_CUSTOMER),
    FIXER(User.COL_USER_TYPE_FIXER)
}
