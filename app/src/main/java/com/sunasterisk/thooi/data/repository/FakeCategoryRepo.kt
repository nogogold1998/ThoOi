package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.source.entity.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Deprecated("")
class FakeCategoryRepo : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = flowOf(listOf(
        foo("Thợ điện lạnh", 2),
        foo("Thợ sửa chữa nước", 1),
        foo("Thợ điện gia dụng", 2),
        foo("Thợ sửa chữa máy tính", 1),
        foo("hayya", 1),
        foo("Thợ sửa khóa", 1),
        foo("Mục khác", 1),
    ))

    override fun getCategoryByIdFlow(id: String): Flow<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryById(id: String): Category? {
        TODO("Not yet implemented")
    }

    private fun foo(bar: String, span: Int) = Category(bar.hashCode().toString(), bar, span)
}
