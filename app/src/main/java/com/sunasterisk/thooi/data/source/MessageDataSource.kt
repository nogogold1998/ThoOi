package com.sunasterisk.thooi.data.source

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.Conversation
import com.sunasterisk.thooi.data.source.entity.Message
import kotlinx.coroutines.flow.Flow

interface MessageDataSource {
    interface Local {

    }

    interface Remote {
        fun getAllConversations(): Flow<Result<List<Conversation>>>

        fun getConversationById(id: String): Flow<Result<Conversation>>

        fun getUserImgUrl(id: String, function: (String) -> Unit)

        suspend fun createNewConversation(conversation: Conversation): Result<DocumentReference>

        fun getMessagesByConversationId(id: String): Flow<Result<List<Message>>>

        suspend fun sendMessage(message: Message): Result<DocumentReference>
    }
}
