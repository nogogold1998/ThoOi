package com.sunasterisk.thooi.data.source.entity

import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.source.remote.RemoteConstants

data class Conversation(
    val id: String = "",
    val lastMessage: String = "",
    val lastTime: String = "",
    val members: List<User> = ArrayList()
) {
    fun getMembersDocumentReference(firestore: FirebaseFirestore) = listOf(
        firestore.collection(RemoteConstants.USERS_COLLECTION).document(members[0].id),
        firestore.collection(RemoteConstants.USERS_COLLECTION).document(members[1].id)
    )
}
