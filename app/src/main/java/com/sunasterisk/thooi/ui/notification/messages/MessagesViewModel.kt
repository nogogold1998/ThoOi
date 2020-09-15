package com.sunasterisk.thooi.ui.notification.messages

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.Conversation
import com.sunasterisk.thooi.data.repository.MessageRepository
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MessagesViewModel(
    private val messageRepository: MessageRepository
) : ViewModel() {

    init {
        getConversations()
    }

    private val _currentUserId = MutableLiveData<String>()

    private val _conversations = MutableLiveData<List<Conversation>>()
    val conversations: LiveData<List<Conversation>> get() = _conversations

    private val _errorRes = MutableLiveData<Event<@StringRes Int>>()
    val errorRes: LiveData<Event<Int>> get() = _errorRes

    fun setCurrentUser(userId: String) = _currentUserId.postValue(userId)

    fun getConversations() {
        viewModelScope.launch {
            _currentUserId.asFlow()
                .flatMapLatest { messageRepository.getAllConversations(it) }
                .collect { result ->
                    result.check(
                        { _conversations.postValue(it) },
                        { _errorRes.postValue(R.string.error_unknown) }
                    )
                }

        }
    }
}
