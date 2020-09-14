package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.PostDataSource
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.entity.PostStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

class PostRepositoryImpl(
    private val remote: PostDataSource.Remote,
    private val local: PostDataSource.Local,
) : PostRepository {
    private val cachedAllPostsFlow = merge(
        remote.getAllPosts().transformLatest {
            if (it is Result.Success) local.savePost(*it.data.toTypedArray())
        },
        local.getAllPosts()
    )

    private val cachedPostFlows = hashMapOf<String, Flow<Post>>()
    override fun getAllPosts(): Flow<List<Post>> {
        return this.cachedAllPostsFlow
    }

    override suspend fun getPostById(id: String): Post? = getPostByIdFlow(id).firstOrNull()

    override fun getPostByIdFlow(id: String): Flow<Post> = synchronized(cachedPostFlows) {
        cachedPostFlows.getOrPut(id) {
            channelFlow {
                launch {
                    remote.getPostById(id).filterIsInstance<Result.Success<Post>>()
                        .distinctUntilChanged()
                        .collect { local.savePost(it.data) }
                }
                launch {
                    local.getPostById(id).collect { offer(it) }
                }
            }
        }
    }

    override fun getPostsByCategories(vararg categories: String): Flow<Result<List<Post>>> {
        TODO("Not yet implemented")
    }

    override fun addNewPost(post: Post): Flow<Result<DocumentReference>> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(post: Post): Result<Unit> = remote.updatePost(post)

    override fun getPostsByUserId(id: String): Flow<Result<List<Post>>> =
        remote.getPostsByCustomer(id)

    override suspend fun updatePostStatus(postId: String, postStatus: PostStatus) {
        remote.changePostStatus(postId, postStatus)
    }
}
