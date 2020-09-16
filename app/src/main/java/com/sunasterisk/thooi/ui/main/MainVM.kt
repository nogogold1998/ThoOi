package com.sunasterisk.thooi.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.getOneShotResult
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainVM(
    firebaseAuth: FirebaseAuth,
    userRepository: UserRepository,
) : ViewModel() {

    private val _isFixer = MutableLiveData<Boolean>()
    val isFixer: LiveData<Boolean> get() = _isFixer

    init {
        firebaseAuth.addAuthStateListener {
            _loginStatus.postValue(it)
            _loggedUserId.offer(it.currentUser?.uid)
        }
        viewModelScope.launch {
            userRepository.getAllUsers().collect { Log.d("SSS", it.size.toString()) }
            userRepository.getCurrentUser().collect { result ->
                result.check({
                    _isFixer.value = it.userType == UserType.FIXER
                }, {
                    _isFixer.value = true
                })
            }
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
