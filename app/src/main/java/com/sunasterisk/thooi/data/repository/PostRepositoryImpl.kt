package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.PostDataSource
import com.sunasterisk.thooi.data.source.entity.Post
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl(
    private val remote: PostDataSource.Remote,
) : PostRepository {
    override fun getAllPosts(): Flow<List<Post>> {
        TODO("Not yet implemented")
    }

    override fun getPostById(id: String): Flow<Post> {
        TODO("Not yet implemented")
    }

    override fun getPostsByCategories(vararg categories: String): Flow<Result<List<Post>>> {
        TODO("Not yet implemented")
    }

    override fun addNewPost(post: Post): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override fun updatePost(post: Post): Flow<Result<Void>> {
        TODO("Not yet implemented")
    }

    override fun getPostsByUserId(id: String): Flow<Result<List<Post>>> =
        remote.getPostsByCustomer(id)
}
