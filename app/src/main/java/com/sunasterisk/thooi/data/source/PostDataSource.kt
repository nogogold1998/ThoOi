package com.sunasterisk.thooi.data.source

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostDataSource {
    interface Local {

    }

    interface Remote {
        suspend fun getAllPosts(categories: List<Category>): Flow<Result<List<Post>>>

        suspend fun getPostsByCustomer(userId: String): Flow<Result<List<Post>>>

        suspend fun getPostById(id: String): Flow<Result<Post>>

        suspend fun addNewPost(post: Post): Result<DocumentReference>

        suspend fun updatePost(post: Post): Result<Unit>
    }
}

