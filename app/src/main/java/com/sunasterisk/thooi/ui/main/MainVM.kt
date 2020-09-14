package com.sunasterisk.thooi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.util.Event
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MainVM(
    firebaseAuth: FirebaseAuth,
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

    fun collapseToolbar() = _toolbarState.postValue(ToolbarState.COLLAPSED)

    fun hideToolbar() = _toolbarState.postValue(ToolbarState.HIDDEN)

    fun showToolbar() = _toolbarState.postValue(ToolbarState.NORMAL)
}

enum class ToolbarState {
    NORMAL, COLLAPSED, HIDDEN
}
