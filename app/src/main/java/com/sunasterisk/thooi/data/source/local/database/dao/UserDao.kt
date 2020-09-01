package com.sunasterisk.thooi.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sunasterisk.thooi.data.source.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg user: User)

    @Query("select * from user")
    suspend fun getAllUsers(): List<User>

    @Query("select * from user")
    fun getAllUsersFlow(): Flow<List<User>>

    @Delete
    suspend fun delete(vararg user: User)

    @Query("delete from user")
    suspend fun deleteAllUsers()
}
