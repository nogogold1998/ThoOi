package com.sunasterisk.thooi.data.source.local.database

import androidx.room.OnConflictStrategy

object DatabaseConstants {

    const val DATABASE_NAME = "tho-oi.db"

    const val DATABASE_VERSION = 1

    const val DEFAULT_CONFLICT_STRATEGY = OnConflictStrategy.REPLACE
}
