package com.sunasterisk.thooi.util

import com.google.firebase.Timestamp
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

fun Timestamp.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this.seconds, 0, ZoneOffset.UTC)

fun Timestamp.toLocalDate(): LocalDate = this.toLocalDateTime().toLocalDate()

fun LocalDateTime.toTimeStamp(): Timestamp =
    Timestamp(this.toEpochSecond(ZoneOffset.UTC), 0)

fun LocalDate.toTimeStamp(): Timestamp =
    LocalDateTime.of(year, month, dayOfMonth, 0, 0).toTimeStamp()
