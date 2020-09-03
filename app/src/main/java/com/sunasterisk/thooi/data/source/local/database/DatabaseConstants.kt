package com.sunasterisk.thooi.data.source.local.database

import androidx.room.OnConflictStrategy
import org.threeten.bp.ZoneOffset

object DatabaseConstants {

    const val DATABASE_NAME = "tho-oi.db"

    const val DATABASE_VERSION = 1

    val DEFAULT_ZONE_OFFSET: ZoneOffset = ZoneOffset.UTC

    const val DEFAULT_CONFLICT_STRATEGY = OnConflictStrategy.REPLACE
}
