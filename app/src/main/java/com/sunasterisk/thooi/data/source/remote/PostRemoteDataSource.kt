package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.PostDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class PostRemoteDataSource(
    database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostDataSource.Remote {

    private val postCollection = database.collection("posts")

    @ExperimentalCoroutinesApi
    override fun getAllPosts(): Flow<Result<List<Post>>> = callbackFlow {

        offer(Result.loading())

        val listener = postCollection.addSnapshotListener { snapshot, exception ->
            snapshot?.let {
                offer(Result.success(snapshot.toObjects(Post::class.java)))
            }

            exception?.let {
                offer(Result.failed(it.message.toString()))
                cancel(it.message.toString())
            }
        }

        awaitClose {
            listener.remove()
            cancel()
        }
    }

    @ExperimentalCoroutinesApi
    override fun getPostById(id: String): Flow<Result<Post>> = callbackFlow {

        offer(Result.loading())

        val listener = postCollection.document(id).addSnapshotListener { snapshot, exception ->
            snapshot?.toObject(Post::class.java)?.let {
                offer(Result.success(it))
            }

            exception?.let {
                offer(Result.failed(it.message.toString()))
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

        val postRef = postCollection.add(post).await()
        emit(Result.success(postRef))
    }.catch {
        emit(Result.failed(it.message.toString()))
    }.flowOn(dispatcher)

    override fun updatePost(post: Post) = flow {
        emit(Result.loading())

        postCollection.document(post.id).set(post).await()
        emit(Result.success(Unit))
    }.catch {
        emit(Result.failed(it.message.toString()))
    }.flowOn(dispatcher)
}
