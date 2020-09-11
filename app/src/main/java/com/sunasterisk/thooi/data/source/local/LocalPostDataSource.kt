package com.sunasterisk.thooi.data.source.local

import com.sunasterisk.thooi.data.source.PostDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.local.database.AppDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalPostDataSource(db: AppDataBase) : PostDataSource.Local {
    private val postDao = db.postDao()

    override fun getAllPosts(): Flow<List<Post>> = postDao.getAllPostsFlow()

    override fun getAllPosts(categories: List<Category>): Flow<List<Post>> {
        val categoryIds = categories.map { it.id }
        return postDao.getAllPostsFlow().map { posts ->
            posts.filter { it.categoryRef in categoryIds }
        }
    }

    override fun getPostsByCustomer(userId: String): Flow<List<Post>> =
        postDao.getAllPostsByCustomerId(userId)

    override fun getPostById(id: String): Flow<Post> = postDao.findPostByIdFlow(id)
}
