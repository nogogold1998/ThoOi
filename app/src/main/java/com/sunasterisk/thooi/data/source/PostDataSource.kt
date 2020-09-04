package com.sunasterisk.thooi.data.source

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostDataSource {
    interface Local {

    }

    interface Remote {
        fun getAllPosts(): Flow<Result<List<Post>>>

        fun getPostById(id: String): Flow<Result<Post>>

        fun addNewPost(post: Post): Flow<Result<DocumentReference>>

        fun updatePost(post: Post): Flow<Result<Unit>>
    }
}

