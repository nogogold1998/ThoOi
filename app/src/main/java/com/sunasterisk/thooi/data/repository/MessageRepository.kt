package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.Conversation
import com.sunasterisk.thooi.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getAllConversations(currentUserId: String): Flow<Result<List<Conversation>>>

    fun getConversationById(
        currentUserId: String,
        id: String
    ): Flow<Result<com.sunasterisk.thooi.data.source.entity.Conversation>>

    suspend fun createNewConversation(conversation: Conversation): Result<DocumentReference>

    fun getMessagesByConversationId(currentUserId: String, id: String): Flow<Result<List<Message>>>

    suspend fun sendMessage(message: com.sunasterisk.thooi.data.source.entity.Message): Result<DocumentReference>

    fun getUserImgUrl(id: String, function: (String) -> Unit)
}
