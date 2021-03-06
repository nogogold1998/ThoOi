package com.sunasterisk.thooi.ui.conversation

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.Message
import com.sunasterisk.thooi.data.repository.MessageRepository
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.postValue
import com.sunasterisk.thooi.util.transform
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val messageRepository: MessageRepository,

    ) : ViewModel() {

    private val _currentUserId = MutableLiveData<String>()

    private val _conversationId = MutableLiveData<String>()

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _imageProfileUrl = MutableLiveData<String>()
    val imageProfileUrl: LiveData<String> get() = _imageProfileUrl

    private val _textProfileName = MutableLiveData<String>()
    val textProfileName: LiveData<String> get() = _textProfileName

    private val _errorRes = MutableLiveData<Event<@StringRes Int>>()
    val errorRes: LiveData<Event<Int>> get() = _errorRes

    val text = MutableLiveData<String>()

    fun setCurrentUser(userId: String) = _currentUserId.postValue(userId)

    fun loadMessages(conversationId: String) {
        _conversationId.postValue(conversationId)

        viewModelScope.launch {
            _currentUserId.asFlow()
                .flatMapLatest { userId ->
                    messageRepository.getMessagesByConversationId(userId, conversationId)
                }
                .collect { result ->
                    result.check(
                        { _messages.postValue(it) },
                        { _errorRes.postValue(R.string.error_unknown) }
                    )
                }
        }
    }

    fun setProfileInfo() {
        viewModelScope.launch {
            _currentUserId.value?.let { userId ->
                _conversationId.value?.let { conversationId ->
                    messageRepository.getConversationById(userId, conversationId)
                        .collect { result ->
                            result.check(
                                {
                                    if (it.members[0].id == userId) {
                                        _imageProfileUrl.postValue(it.members[0].imageUrl)
                                        _textProfileName.postValue(it.members[0].fullName)
                                    } else {
                                        _imageProfileUrl.postValue(it.members[1].imageUrl)
                                        _textProfileName.postValue(it.members[1].fullName)
                                    }
                                },
                                { _errorRes.postValue(R.string.error_unknown) }
                            )
                        }
                }
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            getValidValue()?.let {
                messageRepository.sendMessage(it).check(
                    { text.postValue("") },
                    { _errorRes.postValue(R.string.error_cant_send_message) }
                )
            }
        }
    }

    private suspend fun getValidValue(): com.sunasterisk.thooi.data.source.entity.Message? {
        val textValue = text.value
        val currentUserIdValue = _currentUserId.value
        val conversationIdValue = _conversationId.value

        var senderId = ""
        var receivedId = ""
        textValue?.isNotBlank()?.let {
            messageRepository.getConversationById(
                currentUserIdValue ?: "",
                conversationIdValue ?: ""
            ).collect { result ->
                result.check(
                    {
                        if (it.members[0].id == currentUserIdValue) {
                            senderId = it.members[0].id
                            receivedId = it.members[1].id
                        } else {
                            receivedId = it.members[0].id
                            senderId = it.members[1].id
                        }
                    },
                    { _errorRes.postValue(R.string.error_unknown) }
                )
            }
            return com.sunasterisk.thooi.data.source.entity.Message(
                conversationId = conversationIdValue ?: "",
                senderRef = senderId,
                receiverRef = receivedId,
                text = textValue
            )
        }
        return null
    }
}
