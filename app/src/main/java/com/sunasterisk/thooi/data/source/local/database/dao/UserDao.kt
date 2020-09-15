package com.sunasterisk.thooi.data.source.local.database.dao

import androidx.room.*
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_CONFLICT_STRATEGY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface UserDao {
    @Insert(onConflict = DEFAULT_CONFLICT_STRATEGY)
    suspend fun insert(vararg user: User)

    @Update(onConflict = DEFAULT_CONFLICT_STRATEGY)
    suspend fun update(vararg user: User)

    @Query("select * from user")
    fun getAllUsersFlow(): Flow<List<User>>

    suspend fun getAllUsers() = getAllUsersFlow().first()

    @Query("select * from user where id = :id limit 1")
    fun findUserByIdFlow(id: String): Flow<User>

    suspend fun findUserById(id: String) = findUserByIdFlow(id).firstOrNull()

    @Query("select * from user where id = :id limit 1")
    fun findUserByIdBlocking(id: String): User?

    @Delete
    suspend fun delete(vararg user: User)

    @Query("delete from user")
    suspend fun deleteAllUsers()
}
