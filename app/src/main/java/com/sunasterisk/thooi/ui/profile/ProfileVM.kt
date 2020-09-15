package com.sunasterisk.thooi.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.util.format
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest

class ProfileVM(userRepo: UserRepository) : ViewModel() {

    private val requestedUserId = ConflatedBroadcastChannel<String>()
    val user: LiveData<User> = requestedUserId.asFlow()
        .flatMapLatest { userRepo.getUserFlow(it) }
        .transformLatest { if (it is Result.Success) emit(it.data) }
        .asLiveData()

    val createdDateTime = user.map { it.createdDateTime.format() }

    fun loadUserById(id: String) {
        requestedUserId.offer(id)
    }
}
