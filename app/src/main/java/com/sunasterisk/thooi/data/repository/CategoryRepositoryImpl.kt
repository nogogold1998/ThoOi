package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.CategoryDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class CategoryRepositoryImpl(
    private val remote: CategoryDataSource.Remote,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : CategoryRepository {
    private val categoriesFlow by lazy { remote.getCategories() }

    private val cachedCategoryFlows = hashMapOf<String, Flow<Result<Category>>>()

    override fun getAllCategories(): Flow<Result<List<Category>>> =
        categoriesFlow.flowOn(dispatcher)

    override fun getCategoryByIdFlow(id: String): Flow<Result<Category>> =
        synchronized(cachedCategoryFlows) {
            cachedCategoryFlows.getOrPut(id) {
                remote.getCategoryById(id)
            }
        }
            .flowOn(dispatcher)
}
