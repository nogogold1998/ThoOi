package com.sunasterisk.thooi.data.source

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {
    interface Local {
        suspend fun insertCategory(vararg category: Category)

        fun getCategories(): Flow<List<Category>>

        suspend fun getCategoryById(id: String): Category?

        fun getCategoryByIdFlow(id: String): Flow<Category>
    }

    interface Remote {
        fun getCategories(): Flow<Result<List<Category>>>

        fun getCategoryById(id: String): Flow<Result<Category>>
    }
}
