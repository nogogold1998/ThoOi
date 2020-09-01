package com.sunasterisk.thooi.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.local.database.dao.UserDao

@Database(
    entities = [User::class],
    version = DatabaseConstants.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildAppDataBase(context).also { instance = it }
        }

        private fun buildAppDataBase(context: Context) =
            Room.databaseBuilder(context, AppDataBase::class.java, DatabaseConstants.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
