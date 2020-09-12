package com.sunasterisk.thooi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.postValue

class MainVM(
    firebaseAuth: FirebaseAuth,
) : ViewModel() {

    init {
        firebaseAuth.addAuthStateListener {
            _loginStatus.postValue(it)
            _logInUserId.postValue(it.currentUser?.uid)
        }
    }

    private val _toolbarState = MutableLiveData<ToolbarState>()
    val toolbarState: LiveData<ToolbarState>
        get() = _toolbarState

    private val _loginStatus = MutableLiveData<FirebaseAuth>()
    val loginStatus: LiveData<FirebaseAuth> get() = _loginStatus

    private val _logInUserId = MutableLiveData<Event<String?>>()

    val loginUserId: LiveData<Event<String?>>
        get() = _logInUserId

    fun collapseToolbar() = _toolbarState.postValue(ToolbarState.COLLAPSED)

    fun hideToolbar() = _toolbarState.postValue(ToolbarState.HIDDEN)

    fun showToolbar() = _toolbarState.postValue(ToolbarState.NORMAL)
}

enum class ToolbarState {
    NORMAL, COLLAPSED, HIDDEN
}
