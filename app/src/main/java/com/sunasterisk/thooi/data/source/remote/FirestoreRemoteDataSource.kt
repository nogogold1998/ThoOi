package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code.ABORTED
import com.sunasterisk.thooi.data.source.FirebaseDataSource
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.MSG_FIRESTORE_EXCEPTION
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.POSTS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestorePost
import com.sunasterisk.thooi.util.getOneShotResult
import kotlinx.coroutines.tasks.await

class FirestoreRemoteDataSource(private val firestore: FirebaseFirestore) :
    FirebaseDataSource.Remote {

    private val postCollection by lazy { firestore.collection(POSTS_COLLECTION) }

    private val firestoreException by lazy {
        FirebaseFirestoreException(MSG_FIRESTORE_EXCEPTION, ABORTED)
    }

    override suspend fun addNewPost(post: Post) = getOneShotResult {
        postCollection.add(FirestorePost(post)).await() ?: throw firestoreException
    }

}
