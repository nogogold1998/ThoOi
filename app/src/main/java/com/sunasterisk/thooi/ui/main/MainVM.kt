package com.sunasterisk.thooi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.Event
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transformLatest

class MainVM(
    firebaseAuth: FirebaseAuth,
    userRepository: UserRepository,
) : ViewModel() {

    init {
        firebaseAuth.addAuthStateListener {
            _loginStatus.postValue(it)
        }
    }

    private val _toolbarState = MutableLiveData<ToolbarState>()
    val toolbarState: LiveData<ToolbarState>
        get() = _toolbarState

    private val _loginStatus = MutableLiveData<FirebaseAuth>()
    val loginStatus: LiveData<FirebaseAuth> get() = _loginStatus

    val loginUserId: LiveData<Event<String?>> = userRepository.getCurrentUser()
        .transformLatest { emit(Event((it as? Result.Success)?.data?.id)) }
        .distinctUntilChanged()
        .asLiveData()

    fun collapseToolbar() = _toolbarState.postValue(ToolbarState.COLLAPSED)

    fun hideToolbar() = _toolbarState.postValue(ToolbarState.HIDDEN)

    fun showToolbar() = _toolbarState.postValue(ToolbarState.NORMAL)
}

enum class ToolbarState {
    NORMAL, COLLAPSED, HIDDEN
}
