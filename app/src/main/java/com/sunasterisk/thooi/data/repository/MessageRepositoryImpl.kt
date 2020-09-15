package com.sunasterisk.thooi.data.repository

import com.google.firebase.firestore.DocumentReference
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.Conversation
import com.sunasterisk.thooi.data.model.Message
import com.sunasterisk.thooi.data.source.MessageDataSource
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.util.mapResult
import com.sunasterisk.thooi.util.mapResultList
import kotlinx.coroutines.flow.flow

class MessageRepositoryImpl(
    private val userLocalDataSource: UserDataSource.Local,
    private val local: MessageDataSource.Local,
    private val remote: MessageDataSource.Remote
) : MessageRepository {
    override fun getAllConversations(currentUserId: String) = flow<Result<List<Conversation>>> {
        remote.getAllConversations().mapResultList { Conversation(currentUserId, it) }
    }

    override fun getConversationById(currentUserId: String, id: String) =
        remote.getConversationById(id)

    override suspend fun createNewConversation(conversation: Conversation): Result<DocumentReference> =
        remote.createNewConversation(
            com.sunasterisk.thooi.data.source.entity.Conversation(
                //FIXME Mapping dto to entity OR create new conversation from Cloud function
            )
        )

    override fun getMessagesByConversationId(currentUserId: String, id: String) =
        remote.getMessagesByConversationId(id).mapResultList {
            Message(
                currentUserId,
                userLocalDataSource.getUserBlocking(it.senderRef)?.imageUrl!!,
                it
            )
        }

    override suspend fun sendMessage(message: com.sunasterisk.thooi.data.source.entity.Message): Result<DocumentReference> =
        remote.sendMessage(message)
}
