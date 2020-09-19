package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.MessageDataSource
import com.sunasterisk.thooi.data.source.entity.Conversation
import com.sunasterisk.thooi.data.source.entity.Message
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.CONVERSATIONS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.MESSAGES_COLLECTION
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.USERS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreConversation
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreMessage
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreUser
import com.sunasterisk.thooi.util.getOneShotResult
import com.sunasterisk.thooi.util.toObjectWithId
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class MessageRemoteDataSource(
    auth: FirebaseAuth,
    private val database: FirebaseFirestore
) : MessageDataSource.Remote {

    private val userDocument = auth.uid?.let { database.collection(USERS_COLLECTION).document(it) }
    private val userCollection = database.collection(USERS_COLLECTION)

    private val conversationCollection = database.collection(CONVERSATIONS_COLLECTION)

    override fun getAllConversations(): Flow<Result<List<Conversation>>> = callbackFlow {
        offer(Result.loading())

        val listener =
            userDocument?.addSnapshotListener { snapshot, exception ->
                snapshot?.toObject(FirestoreUser::class.java)?.let { user ->
                    try {
                        offer(Result.success(getConversations(user)))
                    } catch (e: CancellationException) {
                        offer(Result.failed(e))
                    }
                }

                exception?.let {
                    offer(Result.failed(it))
                    cancel(it.toString(), it)
                }
            }
        awaitClose {
            listener?.remove()
            cancel()
        }
    }.flatMapLatest { result ->
        when (result) {
            is Result.Success -> result.data.map { Result.success(it) }
            is Result.Failed -> flowOf(result)
            Result.Loading -> flowOf(Result.Loading)
        }
    }

    override fun getConversationById(id: String): Flow<Result<Conversation>> = callbackFlow {
        conversationCollection.document(id).let {
            getConversationFromConversationDocument(it).map { conversation ->
                getUsersFromConversationDocument(it).map { users ->
                    Conversation(it.id, conversation.lastMessage, conversation.lastTime, users)
                }
            }
        }
        awaitClose {
            cancel()
        }
    }

    override fun getUserImgUrl(id: String, function: (String) -> Unit) {
        userCollection.document(id).get()
            .addOnSuccessListener {
                val url = it?.getString("image_url")
                url?.let { it1 -> function.invoke(it1) }
            }
    }

    override suspend fun createNewConversation(conversation: Conversation) =
        getOneShotResult {
            conversationCollection.add(FirestoreConversation(database, conversation)).await()
        }

    override fun getMessagesByConversationId(id: String) = callbackFlow {
        offer(Result.loading())

        val listener = conversationCollection.document(id).collection(MESSAGES_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                val messages = ArrayList<Message>()
                snapshot?.documents?.forEach { document ->
                    document.toObjectWithId(FirestoreMessage::class.java, Message::class)?.let {
                        messages.add(it)
                    }
                }
                try {
                    offer(Result.success(messages))
                } catch (e: CancellationException) {
                    offer(Result.failed(e))
                }

                exception?.let {
                    offer(Result.failed(it))
                    cancel(it.message.toString())
                }
            }

        awaitClose {
            listener.remove()
            cancel()
        }
    }

    override suspend fun sendMessage(message: Message) =
        getOneShotResult {
            conversationCollection.document(message.conversationId)
                .collection(MESSAGES_COLLECTION)
                .add(FirestoreMessage(database, message)).await()
        }

    private fun getConversations(user: FirestoreUser): Flow<List<Conversation>> =
        user.conversations.map { conversationDocument ->
            getConversationFromConversationDocument(conversationDocument).combine(
                getUsersFromConversationDocument(conversationDocument)
            ) { conversation, users ->
                Conversation(
                    conversationDocument.id,
                    conversation.lastMessage,
                    conversation.lastTime,
                    users
                )
            }
        }

            .toTypedArray()
            .let { arrayOfFlows ->
                combine(*arrayOfFlows) { (it.toList()) }
            }

    private fun getConversationFromConversationDocument(
        conversationDocument: DocumentReference,
    ): Flow<FirestoreConversation> = callbackFlow {
        val listener = conversationDocument.addSnapshotListener { snapshot, exception ->
            snapshot?.toObject(FirestoreConversation::class.java)
                ?.let { offer(it) }

            exception?.let { cancel(it.toString(), it) }
        }
        awaitClose {
            listener.remove()
        }
    }

    private fun getUsersFromConversationDocument(
        conversationDocument: DocumentReference,
    ): Flow<List<User>> = callbackFlow {
        conversationDocument.addSnapshotListener { snapshot, exception ->
            snapshot?.toObject(FirestoreConversation::class.java)
                ?.members
                ?.map { getUserFromUserDocument(it) }
                ?.toTypedArray()
                ?.let { arrayOfFlows ->
                    offer(combine(*arrayOfFlows) { it.toList() })
                }

            exception?.let { cancel(it.toString(), it) }
        }.let { awaitClose { it.remove() } }
    }.flatMapLatest { it }

    private fun getUserFromUserDocument(
        userDocument: DocumentReference,
    ): Flow<User> = callbackFlow {
        userDocument.addSnapshotListener { snapshot, exception ->
            snapshot?.toObject(FirestoreUser::class.java)
                ?.let { offer(User(userDocument.id, it)) }

            exception?.let { cancel(it.toString(), it) }
        }.let { awaitClose { it.remove() } }
    }
}
