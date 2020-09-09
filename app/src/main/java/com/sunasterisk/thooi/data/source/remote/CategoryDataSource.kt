package com.sunasterisk.thooi.data.source.remote

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {
    interface Local {

    }

    interface Remote {
        fun getCategories(): Flow<Result<List<Category>>>

        fun getCategoryById(id: String): Flow<Result<Category>>
    }
}
