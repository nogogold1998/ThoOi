package com.sunasterisk.thooi.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.source.entity.PostStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest

class PostDetailRepositoryImpl(
    private val postRepo: PostRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth,
) : PostDetailRepository {
    override fun getPostDetailById(id: String): Flow<PostDetail> {
        return postRepo.getPostByIdFlow(id).filterNotNull().flatMapLatest { post ->
            val customer = userRepository.getUserFlow(post.customerRef)
                .transformLatest { result ->
                    (result as? Result.Success)?.data?.let { user -> this.emit(user) }
                }

            val list = Array(post.appliedFixerIds.size) { i ->
                post.appliedFixerIds[i]
                    .let { userRepository.getUserFlow(it) }
                    .transformLatest { result ->
                        (result as? Result.Success)?.data?.let { user -> emit(user) }
                    }
            }

            combine(
                customer,
                *list
            ) { users ->
                PostDetail(
                    post.id,
                    post.imagesRefs,
                    post.title,
                    post.createdDateTime,
                    SummaryUser(users[0]),
                    post.description,
                    post.address,
                    users.drop(1).map(::SummaryUser),
                    post.status,
                    post.fixerId,
                    firebaseAuth.currentUser?.uid!!
                )
            }
        }
    }

    override suspend fun assignPostToFixer(postId: String, fixerId: String) {
        postRepo.getPostById(postId)?.copy(fixerId = fixerId, status = PostStatus.PENDING)
            ?.let { postRepo.updatePost(it) }
            ?: throw NullPointerException(".data.repository.PostDetailRepositoryImpl.assignPostToFixer")
    }

    override suspend fun clearAssignedFixer(postId: String) {
        postRepo.getPostById(postId)
            ?.copy(fixerId = "", status = PostStatus.OPEN)
            ?.let { postRepo.updatePost(it) }
            ?: throw NullPointerException(".data.repository.PostDetailRepositoryImpl.clearAssignedFixer")
    }

    override suspend fun cancelFixing(postId: String) {
        postRepo.updatePostStatus(postId, PostStatus.PENDING)
    }

    override suspend fun finishFixing(postId: String) {
        postRepo.updatePostStatus(postId, PostStatus.FINISHED)
    }

    override suspend fun closePost(postId: String) {
        postRepo.updatePostStatus(postId, PostStatus.FINISHED)
    }

    override suspend fun applyJob(postId: String) {
        postRepo.getPostById(postId)?.let {
            val newList = it.appliedFixerIds.toSet() + firebaseAuth.currentUser?.uid
            it.copy(appliedFixerIds = newList.filterNotNull(), status = PostStatus.OPEN)
        }?.let { postRepo.updatePost(it) }
    }

    override suspend fun startFixing(postId: String) {
        postRepo.updatePostStatus(postId, PostStatus.ON_PROGRESS)
    }
}
