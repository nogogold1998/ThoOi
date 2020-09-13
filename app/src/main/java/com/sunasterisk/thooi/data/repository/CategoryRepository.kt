package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<Result<List<Category>>>

    fun getCategoryByIdFlow(id: String): Flow<Result<Category>>
}
