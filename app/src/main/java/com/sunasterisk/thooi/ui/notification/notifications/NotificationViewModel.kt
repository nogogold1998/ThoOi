package com.sunasterisk.thooi.ui.notification.notifications

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.repository.NotificationRepository
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val userRepository: UserRepository,
    private val notificationRepo: NotificationRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            userRepository.getCurrentUser().first().check(
                success = { _userId.postValue(it.id) },
                failed = { _errorRes.postValue(R.string.error_user_not_found) }
            )
        }
    }

    private val _userId = MutableLiveData<String>()

    val notifications = _userId.asFlow()
        .flatMapLatest { notificationRepo.getAllNotifications(it) }
        .catch { _errorRes.postValue(R.string.error_unknown) }
        .asLiveData()

    private val _errorRes = MutableLiveData<Event<@StringRes Int>>()
    val errorRes: LiveData<Event<Int>> get() = _errorRes
}
