package com.sunasterisk.thooi.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sunasterisk.thooi.data.source.entity.Notification
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_CONFLICT_STRATEGY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface NotificationDao {
    @Insert(onConflict = DEFAULT_CONFLICT_STRATEGY)
    suspend fun insert(vararg notification: Notification)

    @Update(onConflict = DEFAULT_CONFLICT_STRATEGY)
    suspend fun update(vararg notification: Notification)

    @Query("select * from notification")
    fun getAllNotificationsFlow(): Flow<List<Notification>>

    suspend fun getAllNotifications() = getAllNotificationsFlow().first()

    @Query("select * from notification where id = :id")
    fun findNotificationByIdFlow(id: String): Flow<Notification>

    suspend fun findNotificationById(id: String) = findNotificationByIdFlow(id).firstOrNull()

    @Delete
    suspend fun delete(vararg notification: Notification)

    @Query("delete from notification")
    suspend fun deleteAllNotifications()
}
