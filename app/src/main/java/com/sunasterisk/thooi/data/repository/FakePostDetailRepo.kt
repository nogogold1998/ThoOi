package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.util.DummyPosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@Deprecated("")
class FakePostDetailRepo : PostDetailRepository {
    private val dispatcher = Dispatchers.Default
    val channel = ConflatedBroadcastChannel<PostDetail>()

    init {
    }

    override fun getPostDetailById(id: String): Flow<PostDetail> = flow {
        DummyPosts.posts.find { it.id == id }?.let {
            PostDetail(
                it.id,
                it.imagesRefs,
                it.title,
                it.appointment,
                DummyPosts.users.random().let {
                    SummaryUser(it.id, it.imageUrl, it.fullName)
                },
                it.description,
                it.address,
                DummyPosts.users.map { SummaryUser(it.id, it.imageUrl, it.fullName) }
            )
        }?.let { emit(it) }
    }

    // fun next() {
    //     val value = channel.value
    //     channel.offer(
    //         when (value.status) {
    //             PostStatus.NEW -> value.copy(appliedFixers = postDetail.appliedFixers,
    //                 status = value.status.nextStatus(),
    //                 assignedFixerId = postDetail.appliedFixers.random().id)
    //             PostStatus.OPEN -> value.copy(status = value.status.nextStatus())
    //             PostStatus.PENDING -> value.copy(status = value.status.nextStatus())
    //             PostStatus.ON_PROGRESS -> value.copy(status = value.status.nextStatus())
    //             PostStatus.FINISHED -> value.copy(status = value.status.nextStatus())
    //         }
    //     )
    // }
    //
    override suspend fun assignPostToFixer(postId: String, fixerId: String): Unit =
        withContext(dispatcher) {
            val post = channel.value
            require(post.id == postId)
            require(post.status == PostStatus.OPEN)
            require(post.appliedFixers.isNotEmpty())
            post.appliedFixers.firstOrNull { it.id == fixerId }
                ?.let {
                    channel.offer(post.copy(assignedFixerId = it.id,
                        status = post.status.nextStatus()))
                }
        }

    override suspend fun clearAssignedFixer(postId: String) {
        withContext(dispatcher) {
            val post = channel.value
            require(post.id == postId)
            require(post.status == PostStatus.PENDING)
            channel.offer(post.copy(assignedFixerId = null, status = post.status.previousStatus()))
        }
    }

    override suspend fun cancelFixing(postId: String) {
        withContext(dispatcher) {
            val post = channel.value
            require(post.id == postId)
            require(post.status == PostStatus.ON_PROGRESS)
            channel.offer(post.copy(status = post.status.previousStatus()))
        }
    }

    override suspend fun finishFixing(postId: String) {
        withContext(dispatcher) {
            val post = channel.value
            require(post.id == postId)
            require(post.status == PostStatus.ON_PROGRESS)
            channel.offer(post.copy(status = post.status.nextStatus()))
        }
    }

    override suspend fun closePost(postId: String) {
        withContext(dispatcher) {
            val post = channel.value
            require(post.id == postId)
            require(post.status in arrayOf(PostStatus.NEW, PostStatus.OPEN))
            channel.offer(post.copy(assignedFixerId = null, status = PostStatus.FINISHED))
        }
    }

    override suspend fun applyJob(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun startFixing(postId: String) {
        TODO("Not yet implemented")
    }
}
