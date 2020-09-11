package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.PostDetail
import kotlinx.coroutines.flow.Flow

/**
 * Created by Cong Vu Chi on 04/09/20 10:27.
 */
interface PostDetailRepository {
    fun getPostDetailById(id: String): Flow<PostDetail>

    suspend fun assignPostToFixer(postId: String, fixerId: String)

    suspend fun clearAssignedFixer(postId: String)

    suspend fun cancelFixing(postId: String)

    suspend fun finishFixing(postId: String)

    suspend fun closePost(postId: String)

    suspend fun applyJob(postId: String)

    suspend fun startFixing(postId: String)
}
