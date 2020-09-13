package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.source.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext

class PostDetailRepositoryImpl(
    private val postRepository: PostRepository,
    private val userLocalDataSource: UserDataSource.Local,
    private val userRepository: UserRepository,
) : PostDetailRepository {
    override fun getPostDetailById(id: String): Flow<PostDetail> {
        return postRepository.getPostById(id).transformLatest {
            val result = withContext(Dispatchers.Main) {
                val customer = async {
                    userLocalDataSource.getUser(it.customerRef)?.let(::SummaryUser)
                }
                // FIXME
                val list = async { it.appliedFixerIds.map { } }

                PostDetail(
                    it.id,
                    it.imagesRefs,
                    it.title,
                    it.createdDateTime,
                    customer.await() ?: SummaryUser(),
                    it.description,
                    it.address,
                    emptyList(),
                    it.status,
                    it.fixerId,
                )
            }
            emit(result)
        }
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
