package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.PostDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.FIELD_CATEGORY
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.FIELD_CUSTOMER
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.FIELD_STATUS
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.MSG_FIRESTORE_EXCEPTION
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.POSTS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestorePost
import com.sunasterisk.thooi.util.getOneShotResult
import com.sunasterisk.thooi.util.getSnapshotFlow
import com.sunasterisk.thooi.util.toObjectWithId
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class PostRemoteDataSource(
    database: FirebaseFirestore,
) : PostDataSource.Remote {

    private val postCollection = database.collection(POSTS_COLLECTION)

    private val firestoreException by lazy {
        FirebaseFirestoreException(MSG_FIRESTORE_EXCEPTION, FirebaseFirestoreException.Code.ABORTED)
    }

    override suspend fun getAllPosts(categories: List<Category>): Flow<Result<List<Post>>> =
        getPostsWithCriteria(
            null,
            Pair(FIELD_CATEGORY, categories.map { it.id }),
            Pair(FIELD_STATUS, listOf(PostStatus.NEW.name, PostStatus.PENDING.name))
        )

    override suspend fun getPostsByCustomer(userId: String): Flow<Result<List<Post>>> =
        getPostsWithCriteria(Pair(FIELD_CUSTOMER, userId))

    override suspend fun getPostById(id: String): Flow<Result<Post>> =
        postCollection.document(id).getSnapshotFlow {
            it.toObjectWithId(FirestorePost::class.java, Post::class)
        }

    override suspend fun addNewPost(post: Post) = getOneShotResult {
        postCollection.add(FirestorePost(post)).await() ?: throw firestoreException
    }

    override suspend fun updatePost(post: Post) = getOneShotResult {
        postCollection.document(post.id).set(FirestorePost(post)).await()
            ?: throw firestoreException
        Unit
    }

    private suspend fun getPostsWithCriteria(
        equalCriteria: Pair<String, String>? = null,
        vararg containCriteria: Pair<String, List<String>>
    ): Flow<Result<List<Post>>> = callbackFlow {
        offer(Result.loading())
        val collection = postCollection.apply {
            equalCriteria?.let { this.whereEqualTo(it.first, it.second) }
            containCriteria.asList().forEach { criteria ->
                criteria.let { whereIn(it.first, it.second) }
            }
        }

        val listener = collection.addSnapshotListener { snapshot, exception ->
            val posts = ArrayList<Post>()
            snapshot?.documents?.forEach { document ->
                document.toObjectWithId(FirestorePost::class.java, Post::class)
                    ?.let { posts.add(it) }
            }
            try {
                offer(Result.success(posts))
            } catch (e: CancellationException) {
                offer(Result.failed(e))
            }

            exception?.let {
                offer(Result.failed(it))
                cancel(it.message.toString())
            }
        }

        awaitClose {
            listener.remove()
            cancel()
        }
    }
}
