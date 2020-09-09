package com.sunasterisk.thooi.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Cong Vu Chi on 04/09/20 16:11.
 */
fun LocalDateTime.format(): String = this.format(
    DateTimeFormatter.ofPattern(TimeConstants.FORMAT_DATE_LONG_TIME_SHORT)
)
