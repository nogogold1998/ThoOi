package com.sunasterisk.thooi.data.source.remote.dto

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.source.entity.Conversation

data class FirestoreConversation(
    val lastMessage: String = "",
    val lastTime: String = "",
    val members: List<DocumentReference> = emptyList()
) {
    constructor() : this(
        "",
        "",
        emptyList()
    )

    constructor(firestore: FirebaseFirestore, conversation: Conversation) : this(
        conversation.lastMessage,
        conversation.lastTime,
        conversation.getMembersDocumentReference(firestore)
    )
}
