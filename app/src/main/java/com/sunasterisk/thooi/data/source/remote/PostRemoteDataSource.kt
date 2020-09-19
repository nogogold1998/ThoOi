package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.firestore.DocumentReference
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostRemoteDataSource(
    database: FirebaseFirestore,
) : PostDataSource.Remote {

    private val postCollection = database.collection(POSTS_COLLECTION)

    private val firestoreException by lazy {
        FirebaseFirestoreException(MSG_FIRESTORE_EXCEPTION, FirebaseFirestoreException.Code.ABORTED)
    }

    private val cachedPostDocuments = hashMapOf<String, DocumentReference>()

    override fun getAllPosts(categories: List<Category>): Flow<Result<List<Post>>> =
        getPostsWithCriteria(
            null,
            Pair(FIELD_CATEGORY, categories.map { it.id }),
            Pair(FIELD_STATUS, listOf(PostStatus.NEW.name, PostStatus.PENDING.name))
        )

    override fun getAllPosts(): Flow<Result<List<Post>>> = callbackFlow {
        offer(Result.loading())
        val listener = postCollection.addSnapshotListener { snapshot, e ->
            try {
                snapshot?.documents
                    ?.mapNotNull { it.toObjectWithId(FirestorePost::class.java, Post::class) }
                    ?.let { offer(Result.success(it)) }
                    ?: throw e!!
            } catch (e: Exception) {
                offer(Result.failed(e))
            }
        }

        awaitClose {
            listener.remove()
            cancel()
        }
    }

    override fun getPostsByCustomer(userId: String): Flow<Result<List<Post>>> =
        getPostsWithCriteria(Pair(FIELD_CUSTOMER, userId))

    override fun getPostById(id: String): Flow<Result<Post>> = synchronized(cachedPostDocuments) {
        cachedPostDocuments.getOrPut(id) { postCollection.document(id) }
    }.getSnapshotFlow { it.toObjectWithId(FirestorePost::class.java, Post::class) }

    override suspend fun addNewPost(post: Post) = getOneShotResult {
        postCollection.add(FirestorePost(post)).await() ?: throw firestoreException
    }

    override suspend fun updatePost(post: Post) = getOneShotResult {
        synchronized(cachedPostDocuments) {
            cachedPostDocuments.getOrPut(post.id) { postCollection.document(post.id) }
        }.set(FirestorePost(post)).await() ?: throw firestoreException
        Unit
    }

    override suspend fun changePostStatus(postId: String, postStatus: PostStatus) {
        cachedPostDocuments
            .getOrPut(postId) { postCollection.document(postId) }
            .update(FIELD_STATUS, postStatus.toString())
            .await()
    }

    private fun getPostsWithCriteria(
        equalCriteria: Pair<String, String>? = null,
        vararg containCriteria: Pair<String, List<String>>,
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
