package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>

    fun getCategoryByIdFlow(id: String): Flow<Category>

    suspend fun getCategoryById(id: String): Category?
}
