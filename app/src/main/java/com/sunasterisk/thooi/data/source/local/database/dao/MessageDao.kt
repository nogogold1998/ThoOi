package com.sunasterisk.thooi.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sunasterisk.thooi.data.source.entity.Message
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_CONFLICT_STRATEGY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface MessageDao {

    @Insert(onConflict = DEFAULT_CONFLICT_STRATEGY)
    suspend fun insert(vararg user: Message)

    @Update(onConflict = DEFAULT_CONFLICT_STRATEGY)
    suspend fun update(vararg user: Message)

    @Query("select * from message")
    fun getAllMessageFlow(): Flow<List<Message>>

    suspend fun getAllMessage() = getAllMessageFlow().first()

    @Query("select * from message where id = :id limit 1")
    fun findMessageByIdFlow(id: String): Flow<Message>

    suspend fun findMessageById(id: String) = findMessageByIdFlow(id).firstOrNull()

    @Delete
    suspend fun delete(vararg user: Message)

    @Query("delete from user")
    suspend fun deleteAllMessages()
}
