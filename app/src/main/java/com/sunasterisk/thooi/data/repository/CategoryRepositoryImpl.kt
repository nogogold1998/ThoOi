package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.source.CategoryDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.util.check
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CategoryRepositoryImpl(
    private val local: CategoryDataSource.Local,
    private val remote: CategoryDataSource.Remote,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = channelFlow {
        launch {
            remote.getCategories()
                .onEach { result ->
                    result.check(
                        success = {
                            runBlocking { local.insertCategory(*it.toTypedArray()) }
                        },
                        failed = { throw it }
                    )
                }
                .flowOn(dispatcher)
                .catch { throw it }
                .conflate()
                .collect()
        }
        local.getCategories().collect { send(it) }
    }

    override fun getCategoryByIdFlow(id: String): Flow<Category> = channelFlow {
        launch {
            remote.getCategoryById(id)
                .onEach { result ->
                    result.check(
                        success = {
                            runBlocking { local.insertCategory(it) }
                        },
                        failed = { throw it }
                    )
                }
                .catch { throw it }
                .distinctUntilChanged()
                .flowOn(dispatcher)
                .conflate()
                .collect()
        }
        local.getCategoryByIdFlow(id).collect { send(it) }
    }

    override suspend fun getCategoryById(id: String): Category? = local.getCategoryById(id)
}
