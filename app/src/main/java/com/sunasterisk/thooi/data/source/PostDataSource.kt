package com.sunasterisk.thooi.data.source

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.entity.PostStatus
import kotlinx.coroutines.flow.Flow

interface PostDataSource {
    interface Local {

        fun getAllPosts(): Flow<List<Post>>

        fun getAllPosts(categories: List<Category>): Flow<List<Post>>

        fun getPostsByCustomer(userId: String): Flow<List<Post>>

        fun getPostById(id: String): Flow<Post>

        suspend fun savePost(vararg post: Post)
    }

    interface Remote {
        fun getAllPosts(categories: List<Category>): Flow<Result<List<Post>>>

        fun getAllPosts(): Flow<Result<List<Post>>>

        fun getPostsByCustomer(userId: String): Flow<Result<List<Post>>>

        fun getPostById(id: String): Flow<Result<Post>>

        suspend fun addNewPost(post: Post): Result<DocumentReference>

        suspend fun updatePost(post: Post): Result<Unit>

        suspend fun changePostStatus(postId: String, postStatus: PostStatus)
    }
}

