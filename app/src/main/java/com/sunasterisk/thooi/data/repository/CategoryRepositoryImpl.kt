package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.CategoryDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val remote: CategoryDataSource.Remote,
) : CategoryRepository {
    override fun getAllCategories(): Flow<Result<List<Category>>> = remote.getCategories()

    override fun getCategoryByIdFlow(id: String): Flow<Result<Category>> =
        remote.getCategoryById(id)
}
