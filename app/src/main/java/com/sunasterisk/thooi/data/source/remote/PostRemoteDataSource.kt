package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.PostDataSource
import com.sunasterisk.thooi.data.source.entity.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.POSTS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestorePost
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class PostRemoteDataSource(
    database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PostDataSource.Remote {

    private val postCollection = database.collection(POSTS_COLLECTION)

    override fun getAllPosts(): Flow<Result<List<Post>>> = callbackFlow {

        offer(Result.loading())

        val listener = postCollection.addSnapshotListener { snapshot, exception ->
            snapshot?.let {
                try {
                    offer(
                        Result.success(
                            it.toObjects(FirestorePost::class.java).map { fireStorePost ->
                                Post(postCollection.id, fireStorePost)
                            })
                    )
                } catch (e: CancellationException) {
                    offer(Result.failed(e))
                }
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

    override fun getPostById(id: String): Flow<Result<Post>> = callbackFlow {

        offer(Result.loading())

        val listener = postCollection.document(id).addSnapshotListener { snapshot, exception ->
            snapshot?.toObject(FirestorePost::class.java)?.let { fireStorePost ->
                try {
                    offer(Result.success(Post(snapshot.id, fireStorePost)))
                } catch (e: CancellationException) {
                    offer(Result.failed(e))
                }
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

    override fun addNewPost(post: Post) = flow<Result<DocumentReference>> {
        emit(Result.loading())

        val postRef = postCollection.add(FirestorePost(post)).await()
        emit(Result.success(postRef))
    }.catch {
        emit(Result.failed(it))
    }.flowOn(dispatcher)

    override fun updatePost(post: Post) = flow {
        emit(Result.loading())

        postCollection.document(post.id).set(FirestorePost(post)).await()
        emit(Result.success(Unit))
    }.catch {
        emit(Result.failed(it))
    }.flowOn(dispatcher)
}
