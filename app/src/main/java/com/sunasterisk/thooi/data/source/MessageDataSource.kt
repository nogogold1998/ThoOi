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

        fun createNewConversation(conversation: Conversation): Flow<Result<DocumentReference>>

        fun sendMessage(
            conversation: Conversation,
            message: Message
        ): Flow<Result<DocumentReference>>
    }
}
