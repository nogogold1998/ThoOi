package com.sunasterisk.thooi.data.source.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.protobuf.ByteString
import com.google.type.LatLng
import com.sunasterisk.thooi.data.source.entity.NotificationType
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_ZONE_OFFSET
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun userTypeToString(type: UserType?) = type?.name

    @TypeConverter
    fun stringToUserType(value: String?) = value?.let(UserType::valueOf)

    @TypeConverter
    fun localDateTimeToLong(dateTime: LocalDateTime?) =
        dateTime?.toEpochSecond(DEFAULT_ZONE_OFFSET)

    @TypeConverter
    fun longToLocalDateTime(value: Long?) = value?.let {
        LocalDateTime.ofEpochSecond(it, 0, DEFAULT_ZONE_OFFSET)
    }

    @TypeConverter
    fun localDateToLong(date: LocalDate?) = date?.toEpochDay()

    @TypeConverter
    fun longToLocalDate(value: Long?) = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun latLngToString(latLng: LatLng?) = latLng?.toByteString()?.toStringUtf8()

    @TypeConverter
    fun stringToLatLng(value: String?) = value
        ?.let(ByteString::copyFromUtf8)
        ?.let(LatLng::parseFrom)

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
