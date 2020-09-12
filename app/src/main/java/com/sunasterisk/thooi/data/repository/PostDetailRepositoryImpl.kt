package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.PostDetail
import kotlinx.coroutines.flow.Flow

class PostDetailRepositoryImpl(
    private val postRepository: PostRepository,
) : PostDetailRepository {
    override fun getPostDetailById(id: String): Flow<PostDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun assignPostToFixer(postId: String, fixerId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAssignedFixer(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun cancelFixing(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun finishFixing(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun closePost(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun applyJob(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun startFixing(postId: String) {
        TODO("Not yet implemented")
    }
}
