package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.CategoryDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val remote: CategoryDataSource.Remote,
) : CategoryRepository {
    private val categoriesFlow by lazy { remote.getCategories() }

    private val cachedCategoryFlows = hashMapOf<String, Flow<Result<Category>>>()

    override fun getAllCategories(): Flow<Result<List<Category>>> = categoriesFlow

    override fun getCategoryByIdFlow(id: String): Flow<Result<Category>> =
        synchronized(cachedCategoryFlows) {
            cachedCategoryFlows.getOrPut(id) {
                remote.getCategoryById(id)
            }
        }
}
