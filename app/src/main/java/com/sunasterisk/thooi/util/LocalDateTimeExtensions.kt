package com.sunasterisk.thooi.util

import com.sunasterisk.thooi.util.TimeConstants.FORMAT_DATE
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Cong Vu Chi on 04/09/20 16:11.
 */
fun LocalDateTime.format(): String = this.format(
    DateTimeFormatter.ofPattern(TimeConstants.FORMAT_DATE_LONG_TIME_SHORT)
)

fun LocalDate.format(): String = format(DateTimeFormatter.ofPattern(FORMAT_DATE))
