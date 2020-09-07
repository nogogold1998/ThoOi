package com.sunasterisk.thooi.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface CategoryDao {
    @Insert(onConflict = DatabaseConstants.DEFAULT_CONFLICT_STRATEGY)
    suspend fun insert(vararg category: Category)

    @Update(onConflict = DatabaseConstants.DEFAULT_CONFLICT_STRATEGY)
    suspend fun update(vararg category: Category)

    @Query("select * from Category")
    fun getAllCategoriesFlow(): Flow<List<Category>>

    suspend fun getAllCategories(): List<Category> = getAllCategoriesFlow().first()

    @Query("select * from Category where id = :id limit 1")
    fun findCategoryByIdFlow(id: String): Flow<Category>

    suspend fun findCategoryById(id: String) = findCategoryByIdFlow(id).firstOrNull()

    @Delete
    suspend fun delete(vararg: Category)

    @Query("delete from Category")
    suspend fun deleteAllCategories()
}
