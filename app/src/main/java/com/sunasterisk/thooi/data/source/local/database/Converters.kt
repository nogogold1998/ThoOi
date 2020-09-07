package com.sunasterisk.thooi.data.source.local.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.sunasterisk.thooi.data.source.entity.NotificationType
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun userTypeToString(type: UserType?) = type?.value

    @TypeConverter
    fun stringToUserType(value: String?) = when (value) {
        User.COL_USER_TYPE_CUSTOMER -> UserType.CUSTOMER
        User.COL_USER_TYPE_FIXER -> UserType.FIXER
        else -> null
    }

    @TypeConverter
    fun localDateTimeToLong(dateTime: LocalDateTime?) =
        dateTime?.toEpochSecond(User.defaultZoneOffset)

    @TypeConverter
    fun longToLocalDateTime(value: Long?) = value?.let {
        LocalDateTime.ofEpochSecond(it, 0, User.defaultZoneOffset)
    }

    @TypeConverter
    fun localDateToLong(date: LocalDate?) = date?.toEpochDay()

    @TypeConverter
    fun longToLocalDate(value: Long?) = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun latLngToString(latLng: LatLng?): String? {
        return latLng?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun stringToLatLng(value: String?): LatLng? {
        return value?.let { gson.fromJson(it, LatLng::class.java) }
    }

    @TypeConverter
    fun listStringToJsonString(list: List<String>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun jsonStringToListString(value: String?): List<String>? = value?.let {
        gson.fromJson(it, Array<String>::class.java).toList()
    }

    @TypeConverter
    fun notificationTypeToString(notificationType: NotificationType?) = notificationType?.name

    @TypeConverter
    fun stringToNotificationType(value: String?) = value?.let(NotificationType::valueOf)

    @TypeConverter
    fun postStatusToString(status: PostStatus?): String? = status?.name

    @TypeConverter
    fun stringToPostStatus(value: String?): PostStatus? = value?.let(PostStatus::valueOf)
}
