package com.sunasterisk.thooi.data.source

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Post

interface FirebaseDataSource {
    interface Remote {
        suspend fun addNewPost(post: Post): Result<DocumentReference>
    }
}
