package com.sunasterisk.thooi.data.source.local

import com.sunasterisk.thooi.data.source.CategoryDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.local.database.AppDataBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

class LocalCategoryDataSourceImpl(
    db: AppDataBase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CategoryDataSource.Local {
    private val categoryDao = db.categoryDao()
    override suspend fun insertCategory(vararg category: Category) = withContext(dispatcher) {
        categoryDao.insert(*category)
    }

    override fun getCategories(): Flow<List<Category>> = categoryDao.getAllCategoriesFlow()

    override suspend fun getCategoryById(id: String): Category? = categoryDao.findCategoryById(id)

    override fun getCategoryByIdFlow(id: String): Flow<Category> =
        categoryDao.findCategoryByIdFlow(id).distinctUntilChanged()
}
