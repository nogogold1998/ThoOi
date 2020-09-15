package com.sunasterisk.thooi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MainVM(
    firebaseAuth: FirebaseAuth,
    userRepository: UserRepository,
) : ViewModel() {

    init {
        firebaseAuth.addAuthStateListener {
            _loginStatus.postValue(it)
            _loggedUserId.offer(it.currentUser?.uid)
        }
    }

    private val _toolbarState = MutableLiveData<ToolbarState>()
    val toolbarState: LiveData<ToolbarState>
        get() = _toolbarState

    private val _loginStatus = MutableLiveData<FirebaseAuth>()
    val loginStatus: LiveData<FirebaseAuth> get() = _loginStatus

    private val _loggedUserId = ConflatedBroadcastChannel<String?>()
    val loginUserId: LiveData<Event<String?>> = _loggedUserId.asFlow()
        .map { Event(it) }
        .distinctUntilChanged()
        .asLiveData()

    private val _scrollToTopEvent = MutableLiveData<Event<Unit>>()

    val scrollToTopEvent: LiveData<Event<Unit>>
        get() = _scrollToTopEvent

    val currentUserType = _loggedUserId.asFlow()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userRepository.getUserFlow(it) }
        .filterNotNull()
        .filterIsInstance<Result.Success<User>>()
        .map { it.data.userType }
        .distinctUntilChanged()
        .asLiveData()

    fun collapseToolbar() = _toolbarState.postValue(ToolbarState.COLLAPSED)

    fun hideToolbar() = _toolbarState.postValue(ToolbarState.HIDDEN)

    fun showToolbar() = _toolbarState.postValue(ToolbarState.NORMAL)

    fun scrollToTop() = _scrollToTopEvent.postValue(Unit)
}

enum class ToolbarState {
    NORMAL, COLLAPSED, HIDDEN
}
