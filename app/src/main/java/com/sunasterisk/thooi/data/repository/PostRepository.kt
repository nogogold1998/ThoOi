package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPosts(): Flow<List<Post>>

    fun getPostById(id: String): Flow<Post>

    fun getPostsByCategories(vararg categories: String): Flow<Result<List<Post>>>

    fun addNewPost(post: Post): Flow<Result<DocumentReference>>

    fun updatePost(post: Post): Flow<Result<Void>>

    fun getPostsByUserId(id: String): Flow<Result<List<Post>>>
}
