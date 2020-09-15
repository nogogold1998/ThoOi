package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Post

interface FirestoreRepository {
    suspend fun newPost(post: Post): Result<DocumentReference>
}
