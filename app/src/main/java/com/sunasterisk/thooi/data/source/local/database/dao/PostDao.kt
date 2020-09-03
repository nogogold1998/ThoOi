package com.sunasterisk.thooi.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sunasterisk.thooi.data.source.entity.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg post: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg post: Post)

    @Query("select * from post")
    fun getAllPostsFlow(): Flow<List<Post>>

    suspend fun getAllPosts(): List<Post> = getAllPostsFlow().first()

    @Query("select * from post where id = :id limit 1")
    fun findPostByIdFlow(id: String): Flow<Post>

    suspend fun findPostById(id: String) = findPostByIdFlow(id).firstOrNull()

    @Delete
    suspend fun delete(vararg: Post)

    @Query("delete from post")
    suspend fun deleteAllPosts()
}
