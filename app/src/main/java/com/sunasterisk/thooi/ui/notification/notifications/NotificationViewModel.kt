package com.sunasterisk.thooi.ui.notification.notifications

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.data.repository.NotificationRepository
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepo: NotificationRepository
) : ViewModel() {

    init {
        getNotifications()
    }

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val _errorRes = MutableLiveData<Event<@StringRes Int>>()
    val errorRes: LiveData<Event<Int>> get() = _errorRes

    fun getNotifications() {
        viewModelScope.launch {
            notificationRepo.getAllNotifications().collect { result ->
                result.check(
                    { _notifications.postValue(it) },
                    { _errorRes.postValue(R.string.error_unknown) }
                )
            }
        }
    }
}
