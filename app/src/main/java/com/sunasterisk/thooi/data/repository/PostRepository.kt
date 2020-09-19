package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.entity.PostStatus
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPosts(): Flow<List<Post>>

    suspend fun getPostById(id: String): Post?

    fun getPostByIdFlow(id: String): Flow<Post>

    fun getPostsByCategories(vararg categories: String): Flow<Result<List<Post>>>

    fun addNewPost(post: Post): Flow<Result<DocumentReference>>

    suspend fun updatePost(post: Post): Result<Unit>

    fun getPostsByUserId(id: String): Flow<Result<List<Post>>>

    suspend fun updatePostStatus(postId: String, postStatus: PostStatus)
}
