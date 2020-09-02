package com.sunasterisk.thooi.data.source.local.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.local.database.dao.UserDao
import java.util.concurrent.Executor

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

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun buildTestAppDataBase(context: Context, testExecutor: Executor? = null) =
            Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java)
                .allowMainThreadQueries()
                .apply {
                    if (testExecutor != null) {
                        setQueryExecutor(testExecutor)
                        setTransactionExecutor(testExecutor)
                    }
                }
                .build()
    }
}
